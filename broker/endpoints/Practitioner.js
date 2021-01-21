const axios = require("../services/axiosInstance.js");
const Practitioner = require("../models/Practitioner");

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
  let prt = req.body.Practitioner;
  let resource = Practitioner.toFHIR(prt);

  axios
    .post(`/Practitioner`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};