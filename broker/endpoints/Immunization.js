// Load configuration settings
const configs = require("../config/server.js");

const axios = configs.axios;
const Immunization = require("../models/Immunization");

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
  let resource = Immunization.toFHIR(imm);

  return axios.post(`/Immunization`, resource).then((response) => {
    return response;
  });
}
