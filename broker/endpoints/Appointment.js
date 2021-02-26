const axios = require("../services/axiosInstance.js");
const Appointment = require("../models/Appointment");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      let r;
      if (response.data.resourceType === "Bundle") {
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
        error: e.response ? e.response.data : e.message
      });
    });
};

exports.create = (req, res) => {
  const appt = req.body.Appointment;
  const resource = Appointment.toFHIR(appt);

  axios
    .post(`/Appointment`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message
      });
    });
};

// Update status.
exports.checkIn = async (req) => {
  const status = "arrived";
  let id;

  // get id of resource to update
  if (req.body.hasOwnProperty("patient")) {
      const config = {
          params: {
              actor: req.body.patient,
              status: "booked"
          }
      };
      id = await axios.get("/Appointment", config).then((response) => {
      const bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Appointment does not exist");
      }
      const resource = bundle.entry[0].resource;
      return resource.id;
    });
  } else {
    return {};
  }

  // patch status and start time
  const patch = [
      {
          op: "add",
          path: "/status",
          value: status
      }
  ];

  // update the database with new appointment
  return axios.patch(`/Appointment/${id}`, patch).then((response) => {
    return Appointment.toModel(response.data);
  });
};

exports.discharge = async (req) => {
  const status = "fulfilled";
  const id = req.query.appointment;

  if (!id) return;

  // patch status and end time
  const patch = [
      {
          op: "add",
          path: "/status",
          value: status
      }
  ];

  // update the database with new appointment
  return axios.patch(`/Appointment/${id}`, patch).then((response) => {
    return Appointment.toModel(response.data);
  });
};
