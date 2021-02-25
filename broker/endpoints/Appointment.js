const axios = require("../services/axiosInstance.js");
const Appointment = require("../models/Appointment");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      let r = [];
      if (response.data.resourceType === "Bundle") {
        if (response.data.entry)
          r = response.data.entry.map((entry) =>
            Appointment.toModel(entry.resource)
          );
      } else {
        r = Appointment.toModel(response.data);
      }
      res.json(r);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

exports.create = (req, res) => {
  let appt = req.body.Appointment;
  let resource = Appointment.toFHIR(appt);

  axios
    .post(`/Appointment`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

// Update status.
exports.checkIn = async (req) => {
  const status = "arrived";
  let endpoint, patch;

  if (req.body.hasOwnProperty("appointment")) endpoint = req.body.appointment;

  // patch status and start time
  patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
  ];

  // update the database with new appointment
  return axios.patch(endpoint, patch).then((response) => {
    return Appointment.toModel(response.data);
  });
};

exports.discharge = async (req) => {
  const status = "fulfilled";
  let id = req.query.appointment;

  if (!id) return;

  // patch status and end time
  let patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
  ];

  // update the database with new appointment
  return axios.patch(`/Appointment/${id}`, patch).then((response) => {
    return Appointment.toModel(response.data);
  });
};
