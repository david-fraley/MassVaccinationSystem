const globals = require("./globals");

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

let urls = [];

async function setup() {
  //
}

async function cleanup() {
  let url;
  for (url of urls) {
    globals.fhirServer.delete(`${url}`).then();
  }
}

async function test() {
  let endpoint;
  for (endpoint of endpoints) {
    let url = `/${endpoint}`;
    let data = JSON.parse(globals.examples[endpoint]);

    globals.broker
      .get(url)
      .then((response) => globals.config(response))
      .catch((error) => globals.info(error));
    globals.broker
      .post(url, data)
      .then((response) => {
        if (response.data.id) urls.push(`${url}/${response.data.id}`);

        globals.config(response);
        console.log(JSON.stringify(response.data));
        console.log("\n");
      })
      .catch((error) => globals.info(error));
  }
}

module.exports = async () => {
  await setup();
  await test();
  await cleanup();
};
