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

exports.checkIn = async (req) => {
  const status = "arrived";
  let id, patch;

  // get id of resource to update
  if (req.query.hasOwnProperty("patient")) {
    let config = {
      params: {
        actor: req.query.patient,
        status: "booked",
      },
    };
    id = await axios.get("/Appointment", config).then((response) => {
      let bundle = response.data;
      let resource;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Appointment does not exist");
      }
      resource = bundle.entry[0].resource;
      return resource.id;
    });
  } else if (req.query.hasOwnProperty("appointment")) {
    id = req.query.appointment;
  } else {
    return {};
  }

  // patch status and start time
  patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
    {
      op: "add",
      path: "/start",
      value: new Date().toISOString(),
    },
  ];

  // update the database with new appointment
  return axios.patch(`/Appointment/${id}`, patch);
};
