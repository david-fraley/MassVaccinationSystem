const axios = require("../services/axiosInstance.js");
const PatientIdService = require("../services/PatientIdService");
const Patient = require("../models/Patient");
const RelatedPerson = require("../models/RelatedPerson");

exports.read = (req, res) => {
  axios
    .get(`${req.url}`)
    .then((response) => {
      let r;
      if (response.data.resourceType === "Bundle") {
        r = response.data.entry.map((entry) => Patient.toModel(entry.resource));
      } else {
        r = Patient.toModel(response.data);
      }
      res.json(r);
    })
    .catch((e) => {
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      });
    });
};

exports.create = (req, res) => {
  createPatients(req, res).catch((e) =>
    res.status(400).json({
      error: e.response ? e.response.data : e.message,
    })
  );
};

async function createPatients(req, res) {
  let patients = req.body.Patient;
  let patient = patients.shift();
  let promises = [];

  if (!patient) {
    res.status(400).json({ error: "at least one patient should be provided" });
    return;
  }

  let resource = Patient.toFHIR(patient);
  let head = (await axios.post(`/Patient`, resource)).data.id;
  console.log(head);
  promises.push(PatientIdService.createQrCode(head));

  // resources for members
  for (patient of patients) {
    promises.push(createMember(patient, head));
  }

  Promise.all(promises).then((results) => {
    res.json({ Patient: results.map((x) => x.qr_code) });
  });
}

// create patient and link to relatedperson
async function createMember(patient, head) {
  // create related person
  let related = {
    patient: `Patient/${head}`,
    relationship: patient.relationship,
  };
  let resource = RelatedPerson.toFHIR(related);
  let relatedID = (await axios.post(`/RelatedPerson`, resource)).data.id;

  // create patient pointing to related person
  patient.link = `RelatedPerson/${relatedID}`;
  resource = Patient.toFHIR(patient);
  let id = (await axios.post(`/Patient`, resource)).data.id;
  console.log(id);

  return PatientIdService.createQrCode(id);
}
