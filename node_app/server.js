const express = require("express");
const axios = require("axios").default;
const Patient = require("./models/Patient");
const RelatedPerson = require("./models/RelatedPerson");

const app = express();
app.use(express.json());
app.set("json spaces", 2);

const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const generalEndpoints = ["/Patient*", "/Organization*"];
const headers = {
  "content-type": "application/fhir+json",
};

// Pass GET requests to HAPI FHIR server
app.get(generalEndpoints, (req, res) => {
  axios
    .get(`${base}${req.url}`)
    .then((response) => {
      // handle success
      res.json(response.data);
    })
    .catch((error) => handleError(res, error));
});

app.post("/Organization", (req, res) => {
  let org = req.body.Organization;
  let resource = {
    resourceType: "Organization",
    active: org.active,
    type: [
      {
        coding: [
          {
            system: "http://hl7.org/fhir/ValueSet/organization-type",
            code: org.type,
            display: org.type,
          },
        ],
      },
    ],
    name: org.name,
  };

  // post resource
  axios
    .post(`${base}/Organization`, resource, headers)
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
    let result = await createPatient_(patient, related);
    response.Patient.push(result.data);

    // update related
    let related_id = result.data.link[0].other.reference.split("/")[1];
    resource = RelatedPerson.toFHIR({
      patient: `Patient/${result.data.id}`,
      relationship: patient.relationship,
    });
    resource.id = related_id;
    await axios.put(`${base}/RelatedPerson/${related_id}`, resource, headers);
  }

  // resources for members
  for (patient of patients) {
    let related = {
      patient: `Patient/${response.Patient[0].id}`,
      relationship: patient.relationship,
    };

    promises.push(createPatient_(patient, related));
  }

  Promise.all(promises).then((results) => {
    for (result of results) {
      response.Patient.push(result.data);
    }
    res.json(response);
  });
}

// create patient and link to relatedperson
async function createPatient_(patient, related) {
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
          .put(`${base}/Encounter/${encounter.id}`, encounter, headers)
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
          .put(`${base}/Appointment/${appt.id}`, appt, headers)
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
