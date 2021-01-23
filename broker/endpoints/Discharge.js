const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Discharge given appt id
module.exports = (req, res) => {
  const encounter_status = "finished";
  const appt_status = "fulfilled";
  let response = {};

  if (req.query.hasOwnProperty("appointment")) {
    let encounter_url = `?appointment=${req.query.appointment}`;
    let appt_url = `/${req.query.appointment}`;

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
  } else {
    response.Encounter = "appointment parameter missing";
    res.status(400).json(response);
  }
};
