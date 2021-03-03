const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Discharge given appt id
module.exports = (req, res) => {
  const response = {};

  if (req.query.appointment && req.query.encounter) {
    Promise.all([Encounter.discharge(req), Appointment.discharge(req)])
      .then((results) => {
        for (let result of results) {
          response[result.resourceType] = result;
        }
        res.json(response);
      })
      .catch((e) => {
        response.error = e.response ? e.response.data : e.message;
        res.status(400).json(response);
      });
  } else {
    response.error = "appointment and encounter ID not specified";
    res.status(400).json(response);
  }
};
