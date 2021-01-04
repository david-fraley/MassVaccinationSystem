const express = require("express");
const axios = require("axios").default;
const Patient = require("./models/Patient");
const RelatedPerson = require("./models/RelatedPerson");
const Organization = require("./models/Organization");
const Encounter = require("./models/Encounter");
const Observation = require("./models/Observation");
const Appointment = require("./models/Appointment");
const Immunization = require("./models/Immunization");

const app = express();
app.use(express.json());
app.set("json spaces", 2);

const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const generalEndpoints = [
  "/Patient*",
  "/Encounter*",
  "/Observation*",
  "/Organization*",
  "/Appointment*",
  "/Immunization*",
];

//Health check endpoint
app.get("/healthcheck", (req, res) => {
  res.send("Success!");
});

// Pass GET requests to HAPI FHIR server
app.get(generalEndpoints, (req, res) => {
  axios
    .get(`${base}${req.url}`)
    .then((response) => {
      res.json(response.data);
    })
    .catch((error) => handleError(res, error));
});

// Create Immunization resource
// Response:  Immunization resource (200)
//            or JSON object with error field (400)
app.post("/Immunization", (req, res) => {
  let imm = req.body.Immunization;
  postImmunization(imm)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

async function postImmunization(imm) {
  let resource = Immunization.toFHIR(imm);

  return axios.post(`${base}/Immunization`, resource).then((response) => {
    return response;
  });
}

app.post("/Appointment", (req, res) => {
  let appt = req.body.Appointment;
  let resource = Appointment.toFHIR(appt);

  axios
    .post(`${base}/Appointment`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Organization", (req, res) => {
  let org = req.body.Organization;
  let resource = Organization.toFHIR(org);

  axios
    .post(`${base}/Organization`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

// Create Observation resource
// and update Immunization resource to reference the new resource.
// Response:  Observation resource (200)
//            or JSON object with error field (400)
app.post("/Observation", (req, res) => {
  let observation = req.body.Observation;
  postObservation(observation)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

async function postObservation(observation) {
  let resource = Observation.toFHIR(observation);

  return axios.post(`${base}/Observation`, resource).then((response) => {
    let ref = observation.partOf;
    // request body for PATCH
    let update = [
      {
        op: "add",
        path: "/reaction",
        value: [{ detail: { reference: `Observation/${response.data.id}` } }],
      },
    ];
    let headers = {
      "content-type": "application/json-patch+json",
    };

    axios.patch(`${base}/${ref}`, update, { headers: headers }).catch((e) => {
      console.log({ error: e.response ? e.response.data : e.message });
    });
    return response;
  });
}

app.post("/Encounter", (req, res) => {
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  axios
    .post(`${base}/Encounter`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Patient", (req, res) => {
  createPatient(req, res).catch((e) =>
    res.status(400).json({
      error: e.response ? e.response.data : e.message,
    })
  );
});

async function createPatient(req, res) {
  let patients = req.body.Patient;
  let patient = patients.shift();
  let promises = [];
  let response = { Patient: [] };

  // resource for head of household
  if (patient) {
    let related = { resourceType: "RelatedPerson" };
    let result = await createPatientWithLink(patient, related);
    response.Patient.push(result.data);

    // update related
    let related_id = result.data.link[0].other.reference.split("/")[1];
    resource = RelatedPerson.toFHIR({
      patient: `Patient/${result.data.id}`,
      relationship: patient.relationship,
    });
    resource.id = related_id;
    await axios.put(`${base}/RelatedPerson/${related_id}`, resource, headers);
  } else {
    res.status(400).json({ error: "at least one patient should be provided" });
    return;
  }

  // resources for members
  for (patient of patients) {
    let related = {
      patient: `Patient/${response.Patient[0].id}`,
      relationship: patient.relationship,
    };

    promises.push(createPatientWithLink(patient, related));
  }

  Promise.all(promises).then((results) => {
    for (result of results) {
      response.Patient.push(result.data);
    }
    res.json(response);
  });
}

// create patient and link to relatedperson
async function createPatientWithLink(patient, related) {
  // create related person
  let resource = RelatedPerson.toFHIR(related);
  let related_id = (
    await axios.post(`${base}/RelatedPerson`, resource, headers)
  ).data.id;

  // create patient pointing to related person
  patient.link = [`RelatedPerson/${related_id}`];
  resource = Patient.toFHIR(patient);
  let result = await axios.post(`${base}/Patient`, resource, headers);

  return result;
}

// Check-in given either
// a) appointment id from QR code
// b) patient id from patient lookup
app.get("/check-in", (req, res) => {
  const encounter_status = "arrived";
  const appointment_status = "arrived";

  if (req.query.hasOwnProperty("patient")) {
    updateEncounterStatus(
      `?subject=${req.query.patient}&status=planned`,
      encounter_status,
      res
    );
    updateAppointmentStatus(
      `?actor=${req.query.patient}&status=booked`,
      appointment_status,
      res
    );
    res.send("");
  } else if (req.query.hasOwnProperty("appointment")) {
    updateEncounterStatus(
      `?appointment=${req.query.appointment}`,
      encounter_status,
      res
    );
    updateAppointmentStatus(
      `/${req.query.appointment}`,
      appointment_status,
      res
    );
    res.send("");
  } else {
    res.send("error");
  }
});

// Given url of Encounter and new status, update encounter resource
// URL is either a specific id '/id'
// or query parameters '?param=value'
function updateEncounterStatus(url, status, res) {
  axios
    .get(`${base}/Encounter${url}`)
    .then((response) => {
      try {
        let encounter;
        if (url.startsWith("/")) {
          encounter = response.data;
        } else {
          let bundle = response.data;
          // update status
          encounter = bundle.entry[0].resource;
        }
        encounter.status = status;
        // update status history

        // update the database with new encounter
        axios
          .put(`${base}/Encounter/${encounter.id}`, encounter)
          .catch((error) => handleError(res, error));
      } catch (e) {
        console.log(e);
      }
    })
    .catch((error) => handleError(res, error))
    .then(function () {
      // always executed
    });
}

function updateAppointmentStatus(url, status, res) {
  axios
    .get(`${base}/Appointment${url}`)
    .then((response) => {
      try {
        let appt;
        if (url.startsWith("/")) {
          appt = response.data;
        } else {
          let bundle = response.data;
          // update status
          appt = bundle.entry[0].resource;
        }
        appt.status = status;

        // update the database with new appointment
        axios
          .put(`${base}/Appointment/${appt.id}`, appt)
          .catch((error) => handleError(res, error));
      } catch (e) {
        console.log(e);
      }
    })
    .catch((error) => handleError(res, error))
    .then(function () {
      // always executed
    });
}

function handleError(res, error) {
  if (error.response) {
    // The request was made and the server responded with a status code
    let errorCode = error.response.status;
    res.status(errorCode).send(error.response.data);
  } else if (error.request) {
    // The request was made but no response was received
    console.log(error.request);
  } else {
    // Something happened in setting up the request that triggered an Error
    console.log("Error", error.message);
  }
  console.log(error.config);
}

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}`));
