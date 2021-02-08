const axios = require("../services/axiosInstance.js");
const PatientIdService = require("../services/PatientIdService");
const Patient = require("../models/Patient");
const RelatedPerson = require("../models/RelatedPerson");
const Appointment = require("../models/Appointment");

const prepend = "x";

exports.prepend = prepend;

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
  let createPromises = [];

  if (!patient) {
    res.status(400).json({ error: "at least one patient should be provided" });
    return;
  }

  // Create head of household to be referenced by other members
  let [head, promises] = await createPatient(patient);
  if (!head) {
    res.status(500).json({ error: "create patient failed" });
    return;
  }

  for (patient of patients) {
    createPromises.push(createPatient(patient, head));
  }

  // Resolve promises and add new promises
  Promise.all(createPromises)
    .then((values) => {
      promises = [].concat.apply(
        promises,
        values.map(([_, newPromises]) => newPromises)
      );

      // Resolve promises and return list of QR codes
      Promise.all(promises).then((results) => {
        let response = {
          Patient: results.reduce((qrCodes, result) => {
            if (result.qr_code) qrCodes.push(result.qr_code);
            return qrCodes;
          }, []),
        };
        res.json(response);
      });
    })
    .catch((e) => res.status(400).json(e));
}

/**
 * Create FHIR Patient resource and return a list
 * containing the ID of the new resource and a list
 * of promises that need to be resolved.
 *
 * @param {Patient information} patient
 * @param {ID of head of household} head
 */
async function createPatient(patient, head) {
  let related;
  let promises = [];

  let id = (await PatientIdService.createPatientId()).patient_id;
  if (!id) return [];

  promises.push(PatientIdService.createQrCodeForPatientId(id));

  // If head is not provided, create RelatedPerson resource first
  // and update reference to patient later.
  if (head == null) related = { resourceType: "RelatedPerson" };
  else
    related = {
      patient: `Patient/${head}`,
      relationship: patient.relationship,
    };

  // Create RelatedPerson resource
  let resource = RelatedPerson.toFHIR(related);
  let relatedID = (await axios.post(`/RelatedPerson`, resource)).data.id;

  // Create Patient resource with ID from PatientIdService
  // and link to RelatedPerson
  let patientID = `${prepend}${id}`;
  patient.link = `RelatedPerson/${relatedID}`;
  resource = Patient.toFHIR(patient);
  resource.id = patientID;
  await axios.put(`/Patient/${patientID}`, resource);

  // If RelatedPerson was not created with a link to a patient,
  // update the resource to link to the new Patient
  if (head == null) {
    head = patientID;
    resource = RelatedPerson.toFHIR({
      patient: `Patient/${head}`,
      relationship: patient.relationship,
    });
    resource.id = relatedID;
    promises.push(axios.put(`/RelatedPerson/${relatedID}`, resource));
  }

  // Mock Appointment resource
  let appointment = {
    status: "booked",
    participant: [
      {
        type: "patient",
        actor: `Patient/${patientID}`,
      },
    ],
  };
  resource = Appointment.toFHIR(appointment);
  promises.push(axios.post(`/Appointment`, resource));

  return [patientID, promises];
}
