const axios = require("../services/axiosInstance.js");
const Encounter = require("../models/Encounter");

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
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  axios
    .post(`/Encounter`, resource)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

// Given url of Encounter and new status, update encounter resource
// URL is either a specific id '/id'
// or query parameters '?param=value'
exports.updateStatus = (url, status) => {
  return axios.get(`/Encounter${url}`).then((response) => {
    let encounter;
    let resourceType = response.data.resourceType;
    let patch;

    if (resourceType === "Encounter") {
      encounter = response.data;
    } else {
      let bundle = response.data;

      if (!bundle.hasOwnProperty("entry")) {
        console.log("Encounter does not exist");
      }
      encounter = bundle.entry[0].resource;
    }

    patch = [
      {
        op: "add",
        path: "/status",
        value: status,
      },
    ];

    // update the database with new encounter
    return axios.patch(`/Encounter/${encounter.id}`, patch).then((response) => {
      return response;
    });
  });
};
