const axios = require("../services/axiosInstance.js");
const Immunization = require("../models/Immunization");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      let r;
      if (response.data.resourceType === "Bundle") {
        r = response.data.entry.map((entry) =>
          Immunization.toModel(entry.resource)
        );
      } else {
        r = Immunization.toModel(response.data);
      }
      res.json(r);
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
  const resource = Immunization.toFHIR(imm);

  return axios.post(`/Immunization`, resource).then((response) => {
    return Immunization.toModel(response.data);
  });
}
