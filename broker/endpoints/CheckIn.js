// Load configuration settings
const configs = require("../config/server.js");

const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Check-in given either
// a) appointment id from QR code
// b) patient id from patient lookup
module.exports = (req, res) => {
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
    Encounter.updateStatus(encounter_url, encounter_status),
    Appointment.updateStatus(appt_url, appt_status),
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
};