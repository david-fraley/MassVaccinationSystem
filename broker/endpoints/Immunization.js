const axios = require("../services/axiosInstance.js");
const ImmunizationCompleted = require("../models/ImmunizationCompleted");
const ImmunizationNotDone = require("../models/ImmunizationNotDone");

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

// Create Immunization resource
// Response:  Immunization resource (200)
//            or JSON object with error field (400)
exports.create = (req, res) => {
  let imm = req.body.Immunization;
  postImmunization(imm)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

async function postImmunization(imm) {
  let resource;
  if (imm.status === "completed") resource = ImmunizationCompleted.toFHIR(imm);
  else if (imm.status === "not-done") resource = ImmunizationNotDone.toFHIR(imm);

  return axios.post(`/Immunization`, resource).then((response) => {
    return response;
  });
}
