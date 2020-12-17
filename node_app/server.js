const express = require("express");
const axios = require("axios").default;

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

app.post("/Patient", (req, res) => {
  let obj = req.body;
  for (patient of obj.Patient) {
    let resource = {
      resourceType: "Patient",
      name: [
        {
          family: patient.family,
          given: [patient.given],
          suffix: [patient.suffix],
        },
      ],
      telecom: [
        // add later
      ],
      gender: patient.gender,
      birthDate: patient.birthDate,
      address: [
        {
          line: [patient.address.line],
          city: patient.address.city,
          state: patient.address.state,
          postalCode: patient.address.postalCode,
          country: patient.address.country,
        },
      ],
      photo: [
        {
          url: "", // add later
          title: `Photo of ${patient.given} ${patient.family} ${patient.suffix}`,
        },
      ],
      contact: [
        {
          relationship: [
            {
              coding: [
                {
                  // unsure
                  system: "http://terminology.hl7.org/CodeSystem/v2-0131",
                  code: patient.contact.relationship,
                  display: patient.contact.relationship,
                },
              ],
            },
          ],
          name: {
            family: patient.contact.family,
            given: [patient.contact.given],
          },
          telecom: [
            {
              system: "phone",
              value: patient.contact.phone.value,
              use: patient.contact.phone.use,
            },
          ],
        },
      ],
      communication: [
        {
          language: {
            text: patient.language,
          },
          preferred: true,
        },
      ],
    };
    // add in telecom
    for (idx in patient.phone) {
      resource.telecom.push({
        system: "phone",
        value: patient.phone[idx].value,
        use: patient.phone[idx].use,
        rank: `${idx}`,
      });
    }
    for (idx in patient.email) {
      resource.telecom.push({
        system: "email",
        value: patient.email[idx],
        rank: `${idx}`,
      });
    }

    // post resource
    axios
      .post(`${base}/Patient`, resource, headers)
      .then((response) => {
        res.json(response.data);
      })
      .catch((e) => res.send(e));
  }
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