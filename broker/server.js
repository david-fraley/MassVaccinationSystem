const express = require("express");
const axios = require("axios").default;
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
const headers = {
  "content-type": "application/fhir+json",
};

//Health check endpoint
app.get("/healthcheck", (req, res) => {
  res.send("Success!");
});

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

app.post("/Immunization", (req, res) => {
  let imm = req.body.Immunization;
  let resource = Immunization.toFHIR(imm);

  // post resource
  axios
    .post(`${base}/Immunization`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Appointment", (req, res) => {
  let appt = req.body.Appointment;
  let resource = Appointment.toFHIR(appt);

  // post resource
  axios
    .post(`${base}/Appointment`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Organization", (req, res) => {
  let org = req.body.Organization;
  let resource = Organization.toFHIR(org);

  // post resource
  axios
    .post(`${base}/Organization`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Observation", (req, res) => {
  let observation = req.body.Observation;
  let resource = Observation.toFHIR(observation);

  // post resource
  axios
    .post(`${base}/Observation`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

app.post("/Encounter", (req, res) => {
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  // post resource
  axios
    .post(`${base}/Encounter`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

// Discharge
app.post("/discharge", (req, res) => {
  const encounter_status = "finished";
  const appt_status = "fulfilled";
  let response = { Encounter: "", Appointment: "" };

  if (req.query.hasOwnProperty("appointment")) {
    let encounter_url = `?appointment=${req.query.appointment}`;
    let appt_url = `/${req.query.appointment}`;

    Promise.all([
      updateEncounterStatus(encounter_url, encounter_status),
      updateAppointmentStatus(appt_url, appt_status),
    ])
      .then((results) => {
        for (result of results) {
          response[result.data.resourceType] = result.data;
        }
        res.json(response);
      })
      .catch((e) => {
        response.error = e.response ? e.response.data : e.message;
        res.status(400).json(response);
      });
  } else {
    response.Encounter = "appointment parameter missing";
    res.status(400).json(response);
  }
});

// Check-in given either
// a) appointment id from QR code
// b) patient id from patient lookup
app.post("/check-in", (req, res) => {
  const encounter_status = "arrived";
  const appt_status = "arrived";
  let encounter_url;
  let appt_url;
  let response = {};

  if (req.query.hasOwnProperty("patient")) {
    encounter_url = `?subject=${req.query.patient}&status=planned`;
    appt_url = `?actor=${req.query.patient}&status=booked`;
  } else if (req.query.hasOwnProperty("appointment")) {
    encounter_url = `?appointment=${req.query.appointment}`;
    appt_url = `/${req.query.appointment}`;
  } else {
    response.error = "patient or appointment parameter missing";
    res.status(400).json(response);
    return;
  }

  Promise.all([
    updateEncounterStatus(encounter_url, encounter_status),
    updateAppointmentStatus(appt_url, appt_status),
  ])
    .then((results) => {
      for (result of results) {
        response[result.data.resourceType] = result.data;
      }
      res.json(response);
    })
    .catch((e) => {
      response.error = e.response ? e.response.data : e.message;
      res.status(400).json(response);
    });
});

// Given url of Encounter and new status, update encounter resource
// URL is either a specific id '/id'
// or query parameters '?param=value'
function updateEncounterStatus(url, status) {
  return axios.get(`${base}/Encounter${url}`).then((response) => {
    let encounter;
    let resourceType = response.data.resourceType;

    if (resourceType === "Encounter") {
      encounter = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        return "Encounter does not exist";
      }
      encounter = bundle.entry[0].resource;
    }
    encounter.status = status;

    // update the database with new encounter
    return axios
      .put(`${base}/Encounter/${encounter.id}`, encounter, headers)
      .then((response) => {
        return response;
      });
  });
}

function updateAppointmentStatus(url, status) {
  return axios.get(`${base}/Appointment${url}`).then((response) => {
    let appt;
    let resourceType = response.data.resourceType;

    if (resourceType === "Appointment") {
      appt = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        return "Appointment does not exist";
      }
      appt = bundle.entry[0].resource;
    }
    appt.status = status;

    // update the database with new appointment
    return axios
      .put(`${base}/Appointment/${appt.id}`, appt, headers)
      .then((response) => {
        return response;
      });
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
