const axios = require("../services/axiosInstance");
const Patient = require("../models/Patient");
const RelatedPerson = require("../models/RelatedPerson");
const {body, param, validationResult, sanitizeBody} = require('express-validator');
const Appointment = require("../models/Appointment");
const uuid = require('uuid');
const tt_service = require('../services/timetapService');

exports.read = [

  param('qrCode', "No QR Code provided for patient lookup").trim().escape().isLength({min: 1}),

  async (req, res) => {
  
    let errors = validationResult(req);
    if(!errors.isEmpty()) {
      return res.status(400).json({error: errors.array()[0].msg});
    }

    try {
        const patientPayload = await findByQrCode(req.params.qrCode);
        const patientRecord = Patient.toModel(patientPayload);
        return res.json(patientRecord);
    } catch (err) {
        res.status(err.status || 400).json({
            error: err.response ? err.response.data : err.message
        });
    }
    return res.status(400).json({
        error: "Patient could not be found."
    });
  }
];

async function findByQrCode(qrCode) {
    if (!qrCode) {
        console.log("Error: no QR code to look up.");
        return {};
    }

	const patientSearchResponse = await axios.get(`/Patient?identifier=${qrCode}`);

    let patientArray;
    if (patientSearchResponse.data.entry) {
        patientArray = patientSearchResponse.data.entry.map((entry) => {
            return Patient.toModel(entry.resource);
        });
    }
    if (!patientArray || patientArray.length !== 1) {
        throw {
            status: 404,
            message: "No patient found with that QR Code."
        };
    }
    const patientId = patientArray[0].id;
    if (!patientId) {
        throw {
            status: 404,
            message: "No patient found with that QR Code."
        };
    }

    const patientPayload = await axios.get(`/Patient/${patientId}`);
    return patientPayload.data;
}

exports.findByQrCode = findByQrCode;

exports.search = [

  // Demonstrates standard usage of express-validator
  // Note that exports.search is now an array of functions, rather than a single function
  // Docs: https://express-validator.github.io/docs/index.html

  body('*').trim().escape(),

  body('firstName', "Please enter the patient's first name").isLength({ min: 1}),
  body('lastName', "Please enter the patient's last name").isLength({ min: 1 }),
  body('birthDate', "Please enter a valid patient birth date").isISO8601({ strict: true}).toDate(),
  body('postalCode', "Invalid postal code entered").optional({checkFalsy: true}).matches(/(^\d{5}$)|(^\d{5}-\d{4}$)/),
  
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
        };
      }
      return res.json({patients: patientArray});
    })
    .catch((err) => {
      res.status(err.status || 400).json({
        error: err.response ? err.response.data: err.message,
      });
    });
  } 

];

exports.create = [
  // Can't just sanitize body since it contains arrays and nested objects
  body('Patient', 'Please provide patient data').custom((patients) => {
    return Array.isArray(patients) && patients.length >=1;
  }),
  body('Patient.*.family', 'Please enter patient last name').trim().escape().isLength({ min: 1}),
  body('Patient.*.given', 'Please enter patient first name').trim().escape().isLength({ min: 1}),
  body('Patient.*.gender', 'Please enter patient gender').trim().escape().custom((genderVal) => {
    return Patient.genderEnums[genderVal];
  }),
  body('Patient.*.birthDate', 'Please enter patient birth date').trim().isDate({format: 'MM/DD/YYYY'}),
  body('Patient.*.race', 'Please enter patient race').trim().escape().custom((raceVal) => {
    return Patient.raceValueSet[raceVal];
  }),
  body('Patient.*.ethnicity', 'Please enter patient ethnicity').trim().escape().custom((ethnicityVal) => {
    return Patient.ethnicityValueSet[ethnicityVal];
  }),
  body('Patient.*.language', 'Please specify patient language').trim().escape().custom((languageVal) => {
    return Patient.languageEnums[languageVal];
  }),
  body('Patient.*.contact.family', 'Please enter emergency contact last name').trim().escape().isLength({min: 1}),
  body('Patient.*.contact.given', 'Please enter emergency contact first name').trim().escape().isLength({min: 1}),
  body('Patient.*.contact.phone.value', 'Please enter valid emergency contact phone number').trim().escape().custom((phoneString) => {
    let numericString = phoneString.replace('(', '').replace(')', '').replace('-', '');
    return numericString.length === 10 && Number(numericString);
  }),
  body('Patient.*.address.use', 'Please enter patient address type').trim().escape().custom((addressType) => {
    return Patient.addressUseEnums[addressType];
  }),
  body('Patient.*.address.line', 'Please enter patient address line').trim().escape().isLength({min: 1}),
  body('Patient.*.address.city', 'Please enter patient address city').trim().escape().isLength({min: 1}),
  body('Patient.*.address.state', 'Please enter patient address state').trim().escape().isLength({min: 1}),
  body('Patient.*.address.postalCode', 'Please enter patient address zip code').trim().escape().matches(/(^\d{5}$)|(^\d{5}-\d{4}$)/),
  body('Patient.*.address.country', 'Please enter patient address country').trim().escape().isLength({min: 1}),
  body('Patient.*.contact.relationship', 'Please specify patient contact relationship').trim().escape().optional({checkFalsy: true}).custom((relationshipVal) => {
    return RelatedPerson.relationshipValueSet[relationshipVal];
  }),

  // Optional fields
  body('Patient.*.middle', '').trim().escape(),
  body('Patient.*.suffix').trim().escape(),
  body('Patient.*.email.*', 'Please enter a valid email').trim().escape().optional({checkFalsy: true}).isEmail(),
  body('Patient.*.phone.*.value', 'Please enter a valid patient phone number').trim().escape().optional({checkFalsy: true}).custom((phoneString) => {
    let numericString = phoneString.replace('(', '').replace(')', '').replace('-', '');
    return numericString.length === 10 && Number(numericString);
  }),
  body('Patient.*.phone.*.use', 'Please enter a valid patient phone type').trim().escape().optional({checkFalsy: true}).custom((phoneUseVal) => {
    return Patient.phoneUseEnums[phoneUseVal];
  }),
  body('Patient.*.contact.phone.use', 'Please enter a valid emergency contact phone type').trim().escape().optional({checkFalsy: true}).custom((phoneUseVal) => {
    return Patient.phoneUseEnums[phoneUseVal];
  }),

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
  const patients = req.body.Patient;
  let patient = patients.shift();
  const promises = [];

  if (!patient) {
    res.status(400).json({ error: "at least one patient should be provided" });
    return;
  }

  // Create head of household to be referenced by other members
  let head = await createPatient(patient);
  if (!head) {
    res.status(500).json({ error: "create patient failed" });
    return;
  }

  for (patient of patients) {
    promises.push(createPatient(patient, head));
  }

  // Resolve promises and send qr_codes as response
  Promise.all(promises)
    .then((values) => {
      const response = {
		    Patient: values.map(value => value.qr_code)
	    };
	  response.Patient.unshift(head.qr_code);
	  res.json(response);
    })
    .catch((e) => res.status(400).json(e));
}

/**
 * Create FHIR Patient resource and return an object
 * containing resourceId and qr_code
 *
 * @param {Patient information} patient
 * @param {IDs of head of household} head
 */
async function createPatient(patient, head) {
  let related;

  // If head is not provided, create RelatedPerson resource first
  // and update reference to patient later.
  if (head == null) related = { resourceType: "RelatedPerson" };
  else
    related = {
      patient: `Patient/${head.resourceId}`,
      relationship: patient.relationship
    };

  // Create RelatedPerson resource
  let resource = RelatedPerson.toFHIR(related);
  const relatedID = (await axios.post(`/RelatedPerson`, resource)).data.id;

  patient.link = `RelatedPerson/${relatedID}`;
  
  resource = Patient.toFHIR(patient);
  
  const patientID = {
      qr_code: uuid.v4()
  };

  // Add QR Code UUID to Patient Identifier list
  if (!resource.identifier) resource.identifier = [];
  resource.identifier.push({
	  value: patientID.qr_code,
	  assigner: {
		  display: "massvaxx"
	  },
	  period: {
		  start: new Date().toISOString()
	  }
  });
  
  const createdPatient = await axios.post(`/Patient`, resource);
  patientID.resourceId = createdPatient.data.id;

  // Create the new Patient/Client record in TimeTap
  await tt_service.createTimetapClient(createdPatient.data);

  // If RelatedPerson was not created with a link to a patient,
  // update the resource to link to the new Patient
  if (head == null) {
    head = patientID;
    resource = RelatedPerson.toFHIR({
      patient: `Patient/${head.resourceId}`,
      relationship: patient.relationship
    });
    resource.id = relatedID;
    axios.put(`/RelatedPerson/${relatedID}`, resource);
  }

  // Mock Appointment resource
  const appointment = {
      status: "booked",
      participant: [
          {
              type: "patient",
              actor: `Patient/${patientID.resourceId}`
          }
      ]
  };
  resource = Appointment.toFHIR(appointment);
  axios.post(`/Appointment`, resource);

  return patientID;
}

exports.update = (req, res) => {
  const patient = Patient.toFHIR(req.body);
  axios
    .put(req.url, patient)
    .then((response) => {
      res.send(Patient.toModel(response.data));
    })
    .catch((e) => {
      console.log(e.response ? e.response.data : e.message);
      res.status(500).send("Failed to update patient");
    });
};