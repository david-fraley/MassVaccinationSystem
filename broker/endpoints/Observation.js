const axios = require("../services/axiosInstance.js");
const Observation = require("../models/Observation");

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

// Create Observation resource
// and update Immunization resource to reference the new resource.
// Response:  Observation resource (200)
//            or JSON object with error field (400)
exports.create = (req, res) => {
    const observation = req.body.Observation;
    postObservation(observation)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

async function postObservation(observation) {
    const resource = Observation.toFHIR(observation);

    return axios.post(`/Observation`, resource).then((response) => {
    const ref = observation.partOf;
    // request body for PATCH
    const patch = [
        {
            op: "add",
            path: "/reaction",
            value: [{ detail: { reference: `Observation/${response.data.id}` } }],
        },
    ];

    axios.patch(`/${ref}`, patch).catch((e) => {
      console.log({ error: e.response ? e.response.data : e.message });
    });
    return response;
  });
}