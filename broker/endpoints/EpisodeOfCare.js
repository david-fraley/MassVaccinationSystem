// Load configuration settings
const configs = require("../config/server.js");

const axios = configs.axios;
const EpisodeOfCare = require("../models/EpisodeOfCare");

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
  let eoc = req.body.EpisodeOfCare;
  let resource = EpisodeOfCare.toFHIR(eoc);

  axios
    .post(`/EpisodeOfCare`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};