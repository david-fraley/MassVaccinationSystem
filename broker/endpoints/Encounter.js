const axios = require("../services/axiosInstance.js");
const Encounter = require("../models/Encounter");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      let r = [];
      if (response.data.resourceType === "Bundle") {
        if (response.data.entry)
          r = response.data.entry.map((entry) =>
            Encounter.toModel(entry.resource)
          );
      } else {
        r = Encounter.toModel(response.data);
      }
      res.json(r);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

exports.post = (req, res) => {
  this.create(req)
    .then((response) => {
      res.json(response);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

exports.create = (req) => {
  let encounter = req.body.Encounter;
  let resource = Encounter.toFHIR(encounter);

  return axios.post(`/Encounter`, resource).then((response) => {
    return Encounter.toModel(response.data);
  });
};

exports.discharge = async (req) => {
  const status = "finished";
  let id = req.query.encounter;

  if (!id) return;

  // patch status and end time
  let patch = [
    {
      op: "add",
      path: "/status",
      value: status,
    },
    {
      op: "add",
      path: "/period/end",
      value: new Date().toISOString(),
    },
  ];

  // update the database with new encounter
  return axios.patch(`/Encounter/${id}`, patch).then((response) => {
    return Encounter.toModel(response.data);
  });
};
