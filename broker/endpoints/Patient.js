const axios = require("../services/axiosInstance.js");
const PatientIdService = require("../services/PatientIdService");
const Patient = require("../models/Patient");
const RelatedPerson = require("../models/RelatedPerson");
const {body, param, validationResult} = require('express-validator');
const Appointment = require("../models/Appointment");

const prepend = "x";

exports.prepend = prepend;

exports.read = [ 
  param('qrCode', "No QR Code provided for patient lookup").trim().escape().isLength({min: 1}),

  (req, res) => {
  
    let errors = validationResult(req);
    if(!errors.isEmpty()) {
      return res.status(400).json({error: errors.array()[0].msg});
    }

    PatientIdService.getPatientIdForQrCode(req.params.qrCode)
    .then((patientIdRecord) => {
      const patientId = patientIdRecord.patient_id;
      if(!patientId) {
        throw {
          status: 404,
          message: "No patient found with that QR Code"
        }
      }
      return axios.get(`/Patient/${prepend}${patientId}`);
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
  }
]

exports.search = [

  // Demonstrates standard usage of express-validator
  // Note that exports.search is now an array of functions, rather than a single function
  // Docs: https://express-validator.github.io/docs/index.html
  body('firstName', "Please enter the patient's first name").trim().escape().isLength({ min: 1}),
  body('lastName', "Please enter the patient's last name").trim().escape().isLength({ min: 1 }),
  body('birthDate', "Please enter a valid patient birth date").trim().escape().isISO8601({ strict: true}).toDate(),
  body('postalCode', "Invalid postal code entered").trim().escape().optional({checkFalsy: true}).matches(/(^\d{5}$)|(^\d{5}-\d{4}$)/),
  
  (req, res) => {

    // Check for errors emitted by the express-validator functions above
    const errors = validationResult(req);
    if(!errors.isEmpty()) {
      let errorString = '';
      errors.array().forEach((error) => {
        errorString += error.msg + ', ';
      });
      return res.status(400).json({error: errorString.slice(0, -2)});
    }

    let endpoint = '/Patient?' +
      'given=' + req.body.firstName +
      '&family=' + req.body.lastName +
      '&birthdate=' + new Date(req.body.birthDate).toISOString().split('T')[0];
  
    if(req.body.postalCode) {
      endpoint += '&address-postalcode=' + req.body.postalCode;
    }

    axios
    .get(endpoint)
    .then((response) => {
      let patientArray;
      if(response.data.entry) {
        patientArray = response.data.entry.map((entry) => {
          return Patient.toModel(entry.resource);
        });
      }
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
]

exports.create = [
  body('Patient', 'Please provide patient data').custom((patients) => {
    return Array.isArray(patients) && patients.length >=1;
  }),
  body('Patient.*.family', "Please fill out patient last name").trim().escape().isLength({ min: 1}),
  body('Patient.*.given', "Please fill out patient first name").trim().escape().isLength({ min: 1}),
  body('Patient.*.gender', "Please fill out patient gender").trim().escape().custom((genderVal) => {
    return Patient.genderEnums[genderVal];
  }),
  body('Patient.*.birthDate', 'Please fill out patient birth date').trim().isDate({format: 'MM/DD/YYYY'}),
  body('Patient.*.race', "Please fill out patient race").trim().escape().isLength({min: 1}),
  body('Patient.*.ethnicity', "Please fill out patient ethnicity").trim().escape().isLength({min: 1}),
  body('Patient.*.language', "Please specify patient language").trim().escape().custom((languageVal) => {
    return Patient.languageEnums[languageVal];
  }),
  body('Patient.*.contact.family', "Please fill out emergency contact last name").trim().escape().isLength({min: 1}),
  body('Patient.*.contact.given', "Please fill out emergency contact first name").trim().escape().isLength({min: 1}),
  body('Patient.*.contact.phone.value', "Please fill out emergency contact phone number").trim().escape().custom((phoneString) => {
    let numericString = phoneString.replace('(', '').replace(')', '').replace('-', '');
    return Number(numericString);
  }),
  body('Patient.*.address.use', "Please fill out patient address type").trim().escape().custom((addressType) => {
    return Patient.addressUseEnums[addressType];
  }),
  body('Patient.*.address.line', "Please fill out patient address line").trim().escape().isLength({min: 1}),
  body('Patient.*.address.city', "Please fill out patient address city").trim().escape().isLength({min: 1}),
  body('Patient.*.address.state', "Please fill out patient address state").trim().escape().isLength({min: 1}),
  body('Patient.*.address.postalCode', "Please fill out patient address zip code").trim().escape().matches(/(^\d{5}$)|(^\d{5}-\d{4}$)/),
  body('Patient.*.address.country', "Please fill out patient address country").trim().escape().isLength({min: 1}),


  (req, res) => {

    const errors = validationResult(req);
    if(!errors.isEmpty()) {
      let errorString = '';
      errors.array().forEach((error) => {
        errorString += error.msg + ', ';
      });
      return res.status(400).json({error: errorString.slice(0, -2)});
    }

    createPatients(req, res).catch((e) =>
      res.status(400).json({
        error: e.response ? e.response.data : e.message,
      })
    );
  }
]

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
