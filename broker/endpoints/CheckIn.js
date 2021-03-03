const Appointment = require("./Appointment");
const Encounter = require("./Encounter");

// Check-in updates Appointment status and creates an Encounter
module.exports = (req, res) => {
  Appointment.checkIn(req)
    .then((appointment) => {
      req.body.appointment = `Appointment/${appointment.id}`;
      req.body.Encounter = req.body;
      Encounter.create(req).then((encounter) => {
        res.json({
          Encounter: encounter,
          Appointment: appointment,
        });
      });
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};
