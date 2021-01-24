const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Discharge given appt id
module.exports = (req, res) => {
  let response = {};

  if (req.query.hasOwnProperty("appointment")) {
    Promise.all([
      Encounter.discharge(req),
      Appointment.discharge(req),
    ])
      .then((results) => {
        for (result of results) {
          response[result.resourceType] = result;
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
};
