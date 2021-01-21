const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Check-in given either
// a) appointment id from QR code
// b) patient id from patient lookup
module.exports = (req, res) => {
  let response = {};

  if (!req.query.hasOwnProperty("patient") && !req.query.hasOwnProperty("appointment")) {
    response.error = "patient or appointment parameter missing";
    res.status(400).json(response);
    return;
  }

  Promise.all([
    Encounter.checkIn(req),
    Appointment.checkIn(req),
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