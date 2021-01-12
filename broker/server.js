// Load configuration settings
const configs = require("./config/server.js");

const express = require("express");
const axios = require("axios").default;
const Patient = require("./endpoints/Patient");
const Organization = require("./models/Organization");
const Encounter = require("./models/Encounter");
const Observation = require("./models/Observation");
const Appointment = require("./models/Appointment");
const Immunization = require("./models/Immunization");
const EpisodeOfCare = require("./models/EpisodeOfCare");
const Practitioner = require("./models/Practitioner");
const SendHL7Message = require("./endpoints/SendHL7Message");

const app = express();
app.use(express.json());
app.set("json spaces", 2);

if (process.env.DEVELOPMENT == 1) {
  const reg_path = __dirname + "/PatientRegistrationViews/";
  const pod_path = __dirname + "/PointOfDispensingViews/";

  app.use("/Registration", express.static(reg_path));
  app.use("/POD", express.static(pod_path));

  app.get("/broker/*", (req, res) => {
    req.url = req.url.replace(/^(\/broker)/, "");
    app.handle(req, res);
  });
}

const generalEndpoints = [
  "/Encounter*",
  "/Observation*",
  "/Organization*",
  "/Appointment*",
  "/Immunization*",
  "/EpisodeOfCare*",
  "/Practitioner*",
];
const patchHeaders = {
  "content-type": "application/json-patch+json",
};

//Health check endpoint
app.get("/healthcheck", (req, res) => {
  res.send("Success!");
});

// Pass GET requests to HAPI FHIR server
app.get(generalEndpoints, (req, res) => {
  axios
    .get(`${configs.fhirUrlBase}${req.url}`)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

app.post("/Patient", Patient.create);
app.get("/Patient*", Patient.read);

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

  return axios.post(`${configs.fhirUrlBase}/Immunization`, resource).then((response) => {
    return response;
  });
}

app.post("/Appointment", (req, res) => {
  let appt = req.body.Appointment;
  let resource = Appointment.toFHIR(appt);

  axios
    .post(`${configs.fhirUrlBase}/Appointment`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

app.post("/Organization", (req, res) => {
  let org = req.body.Organization;
  let resource = Organization.toFHIR(org);

  axios
    .post(`${configs.fhirUrlBase}/Organization`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
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

// Tell Mirth to send an HL7 message
app.post("/SendHL7Message", SendHL7Message);

async function postObservation(observation) {
  let resource = Observation.toFHIR(observation);

  return axios
    .post(`${configs.fhirUrlBase}/Observation`, resource)
    .then((response) => {
      let ref = observation.partOf;
      // request body for PATCH
      let update = [
        {
          op: "add",
          path: "/reaction",
          value: [{ detail: { reference: `Observation/${response.data.id}` } }],
        },
      ];

      axios
        .patch(`${configs.fhirUrlBase}/${ref}`, update, { headers: patchHeaders })
        .catch((e) => {
          console.log({ error: e.response ? e.response.data : e.message });
        });
      return response;
    });
}

app.post("/Encounter", (req, res) => {
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  axios
    .post(`${configs.fhirUrlBase}/Encounter`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

app.post("/EpisodeOfCare", (req, res) => {
  let eoc = req.body.EpisodeOfCare;
  let resource = EpisodeOfCare.toFHIR(eoc);

  axios
    .post(`${configs.fhirUrlBase}/EpisodeOfCare`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
});

app.post("/Practitioner", (req, res) => {
  let prt = req.body.Practitioner;
  let resource = Practitioner.toFHIR(prt);

  axios
    .post(`${configs.fhirUrlBase}/Practitioner`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
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
  return axios.get(`${configs.fhirUrlBase}/Encounter${url}`).then((response) => {
    let encounter;
    let resourceType = response.data.resourceType;
    let patch;

    if (resourceType === "Encounter") {
      encounter = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Encounter does not exist");
      }
      encounter = bundle.entry[0].resource;
    }

    patch = [
      {
        op: "add",
        path: "/status",
        value: status,
      },
    ];

    // update the database with new encounter
    return axios
      .patch(`${configs.fhirUrlBase}/Encounter/${encounter.id}`, patch, {
        headers: patchHeaders,
      })
      .then((response) => {
        return response;
      });
  });
}

function updateAppointmentStatus(url, status) {
  return axios.get(`${configs.fhirUrlBase}/Appointment${url}`).then((response) => {
    let appt;
    let resourceType = response.data.resourceType;
    let patch;

    if (resourceType === "Appointment") {
      appt = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Appointment does not exist");
      }
      appt = bundle.entry[0].resource;
    }

    patch = [
      {
        op: "add",
        path: "/status",
        value: status,
      },
    ];

    // update the database with new appointment
    return axios
      .patch(`${configs.fhirUrlBase}/Appointment/${appt.id}`, patch, { headers: patchHeaders })
      .then((response) => {
        return response;
      });
  });
}

app.listen(configs.port, () =>
  console.log(`Listening on port ${configs.port}`)
);
