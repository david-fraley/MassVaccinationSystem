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
  await setup();
  await test();
  await cleanup();
};
