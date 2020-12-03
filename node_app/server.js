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

// Check-in given either
// a) appointment id from QR code
// b) patient id from patient lookup
app.get("/check-in", (req, res) => {
  let url;
  if (req.query.hasOwnProperty("patient")) {
    url = `${base}/Appointment?actor=${req.query.patient}&status=booked`;
  } else if (req.query.hasOwnProperty("appointment")) {
    url = `${base}/Appointment/${req.query.appointment}`;
  }

  if (url) {
    axios
      .get(url)
      .then((response) => {
        checkin(response.data, res);
      })
      .catch((error) => handleError(res, error))
      .then(function () {
        // always executed
      });
  } else {
    res.send("error");
  }
});

function checkin(bundle, res) {
  // update appointment
  try {
    // update status to checked-in
    let appt = bundle.entry[0].resource;
    appt.status = "checked-in";

    // update the database with new appointment
    axios
      .put(`${base}/Appointment/${appt.id}`, appt, headers)
      .catch((error) => handleError(res, error));

    // update encounter
    axios
      .get(`${base}/Encounter?appointment=${appt.id}`)
      .then((response) => {
        let bundle = response.data;
        // update status to in-progress
        let encounter = bundle.entry[0].resource;
        encounter.status = "arrived";
        // update status history

        // update the database with new encounter
        axios
          .put(`${base}/Encounter/${encounter.id}`, encounter, headers)
          .catch((error) => handleError(res, error));
      })
      .catch((error) => handleError(res, error))
      .then(function () {
        // always executed
      });
    res.send("success");
  } catch (e) {
    // handle error
    console.log(bundle);
    console.log(e);
  }
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
