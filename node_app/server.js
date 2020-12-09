const express = require("express");
const axios = require("axios").default;

const app = express();
app.use(express.json());
app.set("json spaces", 2);

const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const generalEndpoints = ["/Patient*"];
const headers = {
  "content-type": "application/fhir+json",
};

// Pass request to HAPI FHIR server
app.all(generalEndpoints, (req, res) => {
  axios({
    method: req.method,
    url: `${base}${req.url}`,
    data: req.body,
    headers: headers,
  })
    .then((response) => {
      // handle success
      res.json(response.data);
    })
    .catch((error) => handleError(res, error));
});

app.post("/Immunization", (req, res) => {
  let imm = req.body.Immunization;
  let resource = {
    resourceType: "Immunization",
    vaccineCode: {
      coding: [
        {
          system: "",
          code: imm.vaccine,
          display: imm.vaccine,
        },
      ],
    },
    manufacturer: {
      reference: imm.manufacturer,
    },
    lotNumber: imm.lotNumber,
    expirationDate: imm.expiration,
    patient: {
      reference: imm.patient,
    },
    encounter: {
      reference: imm.encounter,
    },
    status: imm.status,
    statusReason: {
      coding: [
        {
          system: "https://www.hl7.org/fhir/v3/ActReason/cs.html",
          code: imm.statusReason,
          display: imm.statusReason,
        },
      ],
    },
    occurrenceDateTime: imm.occurrence,
    primarySource: imm.primarySource,
    location: {
      reference: imm.location,
    },
    site: {
      coding: [
        {
          system: "https://www.hl7.org/fhir/v3/ActSite/cs.html",
          code: imm.site,
          display: imm.site,
        },
      ],
    },
    route: {
      coding: [
        {
          system: "https://www.hl7.org/fhir/v3/RouteOfAdministration/cs.html",
          code: imm.route,
          display: imm.route,
        },
      ],
    },
    doseQuantity: {
      value: imm.doseQuantity,
      system: "http://unitsofmeasure.org",
      code: imm.doseUnit,
    },
    performer: [
      // add later
    ],
    education: [
      // add later
    ],
    protocolApplied: [
      {
        series: imm.series,
        doseNumberPositiveInt: imm.doseNumber,
      },
    ],
  };

  // add performer
  let performer;
  for (performer of imm.performer) {
    resource.performer.push({
      actor: {
        reference: performer,
      },
    });
  }
  // add education
  let education;
  for (education of imm.education) {
    resource.education.push({
      reference: education,
    });
  }
  // post resource
  axios
    .post(`${base}/Immunization`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));
});

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
