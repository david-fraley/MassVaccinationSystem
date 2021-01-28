const axios = require("../services/axiosInstance.js");
const Patient = require("../models/Patient");
const RelatedPerson = require("../models/RelatedPerson");
const PatientIdService = require("../services/PatientIdService");

exports.read = (req, res) => {
  
  if(!req.params.qrCode) {
    return res.status(400).json({
      error: "No QR Code provided for patient lookup"
    })
  }

  PatientIdService.getPatientId(req.params.qrCode)
  .then((patientIdRecord) => {
    const patientId = patientIdRecord.patient_id;
    if(!patientId) {
      throw {
        status: 404,
        message: "No patient found with that QR Code"
      }
    }
    return axios.get(`${process.env.FHIR_URL_BASE}/Patient/${patientId}`);
  })
  .then((patientPayload) => {
    const patientRecord = Patient.toModel(patientPayload.data);
    return res.json(patientRecord);
  })
  .catch((err) => {
    res.status(err.status || 400).json({
      error: err.response ? err.response.data : err.message,
    });
  });
};

exports.search = (req, res) => {

  const patientSearchPayload = {
    firstName: req.body.firstName,
    lastName: req.body.lastName,
    birthDate: req.body.birthDate,
    postalCode: req.body.postalCode
  };
  
  // We should validate that all required info was sent from the client
  // https://express-validator.github.io/docs/ ??

  // Use POST request so no PHI is included in URL query parameters
  axios
    .post(`${process.env.FHIR_URL_BASE}/SearchPatients`, patientSearchPayload)
    .then((response) => {
      let patientArray = response.data.entry.map((entry) => {
        Patient.toModel(entry.resource);
      });
      if(!patientArray || patientArray.length === 0) {
        throw {
          status: 404,
          message: "We couldn't find any patients with that information"
        }
      }
      return res.json({patients: patientArray});
    })
    .catch((err) => {
      res.status(err.status || 400).json({
        error: err.response ? err.response.data: err.message,
      });
    })
}

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
    response.Patient.push(result);

    // update related
    let related_id = result.link.split("/")[1];
    let resource = RelatedPerson.toFHIR({
      patient: `Patient/${result.id}`,
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
      response.Patient.push(result);
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

  return Patient.toModel(result.data);
}
