const globals = require("./globals");

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
  "Practitioner",
];

// List of created resources - for cleanup
let urls = [];

/**
 * Delete resources created from this test
*/
async function cleanup() {
  let promises = [];

  let url;
  for (url of urls) {
    let promise = globals.fhirServer.delete(`${url}`);
    promises.push(promise);
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
  let promises = [];

  let endpoint;
  for (endpoint of endpoints) {
    let url = `/${endpoint}`;
    let data = JSON.parse(globals.examples[endpoint]);

    let read = globals.broker
      .get(url)
      .then((response) => globals.config(response))
      .catch((error) => globals.info(error));
    let create = globals.broker
      .post(url, data)
      .then((response) => {
        if (response.data.id) urls.push(`${url}/${response.data.id}`);
        else if (response.data.Patient) {
          let patient;
          let patients = response.data.Patient;
          for (patient of patients) {
            urls.push(`${url}/${patient.id}`);
            // Can't delete both for some reason
            // urls.push(`/${patient.link}`);
          }
        }

        globals.config(response);
        console.log(JSON.stringify(response.data));
        console.log("\n");
      })
      .catch((error) => globals.info(error));
    promises.push(read, create);
  }

  await Promise.all(promises);
}

module.exports = async () => {
  await test();
  await cleanup();
};
