const globals = require("./globals");
const Patient = require("../endpoints/Patient");

// Endpoints to test
const endpoints = [
  "Appointment",
  "Encounter",
  "EpisodeOfCare",
  "Immunization",
  "Location",
  "Observation",
  "Organization",
  "Patient",
  "Practitioner"
];

// List of created resources - for cleanup
let urls = [];

/**
 * Add Patients to list of resources to delete.
 *
 * @param {List of QR codes of patients} qrCodes
 */
async function markPatients(qrCodes) {
  for (let code of qrCodes) {
    urls.push(`/Patient/${code}`);
  }
}

/**
 * Delete resources created from this test
 */
async function cleanup() {
  const promises = [];

  let url;
  for (url of urls) {
    let promise;
    
    if (url.includes("Patient")) {
      const patientIdRecord = await Patient.findByQrCode(url.split('/')[2]);
      promise = globals.fhirServer.delete(`/Patient/${patientIdRecord.id}`);
      try {
        await promise;
      }
      catch(error) {
        console.log("generalTest cleanup failed");
        globals.info(error);
      }
    }
    else {
      promise = globals.fhirServer.delete(`${url}`);
      promises.push(promise);
    }
  }

  await Promise.all(promises).catch((error) => {
    console.log("generalTest cleanup failed");
    globals.info(error);
  });
}

/**
 * Send GET and POST requests to multiple endpoints
 */
async function test() {
  const promises = [];

  let endpoint;
  for (endpoint of endpoints) {
    const url = `/${endpoint}`;
    const data = JSON.parse(globals.examples[endpoint]);

    if(endpoint !== 'Patient') {
        const read = new globals.broker
            .get(url)
            .then((response) => globals.config(response))
            .catch((error) => globals.info(error));
        promises.push(read);
    }

    const create = globals.broker
        .post(url, data)
        .then(async (response) => {
            if (response.data.id) urls.push(`${url}/${response.data.id}`);
            else if (response.data.Patient)
              // wait for references to be added to cleanup list
              await markPatients(response.data.Patient);

            globals.config(response);
            console.log(JSON.stringify(response.data));
            console.log("\n");
        })
        .catch((error) => globals.info(error));
    promises.push(create);
  }

  await Promise.all(promises);
}

module.exports = async () => {
  await test();
  await cleanup();
};
