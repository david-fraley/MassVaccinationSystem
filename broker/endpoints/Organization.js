const axios = require("../services/axiosInstance.js");
const Organization = require("../models/Organization");

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
  let org = req.body.Organization;
  let resource = Organization.toFHIR(org);

  axios
    .post(`/Organization`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};