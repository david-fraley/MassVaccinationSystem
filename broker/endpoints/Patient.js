// Load configuration settings
const configs = require("../config/server.js");

const axios = configs.axios;
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
  createPatient(req, res).catch((e) =>
    res.status(400).json({
      error: e.response ? e.response.data : e.message,
    })
  );
};

async function createPatient(req, res) {
  let patients = req.body.Patient;
  let patient = patients.shift();
  let promises = [];
  let response = { Patient: [] };

  // resource for head of household
  if (patient) {
    let related = { resourceType: "RelatedPerson" };
    let result = await createPatientWithLink(patient, related);
    response.Patient.push(result.data);

    // update related
    let related_id = result.data.link[0].other.reference.split("/")[1];
    resource = RelatedPerson.toFHIR({
      patient: `Patient/${result.data.id}`,
      relationship: patient.relationship,
    });
    resource.id = related_id;
    await axios.put(`/RelatedPerson/${related_id}`, resource);
  } else {
    res.status(400).json({ error: "at least one patient should be provided" });
    return;
  }

  // resources for members
  for (patient of patients) {
    let related = {
      patient: `Patient/${response.Patient[0].id}`,
      relationship: patient.relationship,
    };

    promises.push(createPatientWithLink(patient, related));
  }

  Promise.all(promises).then((results) => {
    for (result of results) {
      response.Patient.push(result.data);
    }
    res.json(response);
  });
}

// create patient and link to relatedperson
async function createPatientWithLink(patient, related) {
  // create related person
  let resource = RelatedPerson.toFHIR(related);
  let related_id = (await axios.post(`/RelatedPerson`, resource)).data.id;

  // create patient pointing to related person
  patient.link = [`RelatedPerson/${related_id}`];
  resource = Patient.toFHIR(patient);
  let result = await axios.post(`/Patient`, resource);

  return result;
}
