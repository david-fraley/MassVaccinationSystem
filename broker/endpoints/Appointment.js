const axios = require("../services/axiosInstance.js");
const Appointment = require("../models/Appointment");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      res.json(response.data);
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

exports.updateStatus = (url, status) => {
  return axios.get(`/Appointment${url}`).then((response) => {
    let appt;
    let resourceType = response.data.resourceType;
    let patch;

    if (resourceType === "Appointment") {
      appt = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Appointment does not exist");
      }
      appt = bundle.entry[0].resource;
    }

    patch = [
      {
        op: "add",
        path: "/status",
        value: status,
      },
    ];

    // update the database with new appointment
    return axios.patch(`/Appointment/${appt.id}`, patch).then((response) => {
      return response;
    });
  });
};
