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
    const imm = req.body.Immunization;
    postImmunization(imm)
    .then((response) => {
      res.json(response);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

async function postImmunization(imm) {
  let immunization;
  if (imm.status === "completed") immunization = ImmunizationCompleted;
  else if (imm.status === "not-done") immunization = ImmunizationNotDone;
  else throw "Invalid immunization type";

  const resource = immunization.toFHIR(imm);

  return axios.post(`/Immunization`, resource).then((response) => {
    return immunization.toModel(response.data);
  });
}
