// Pre-requisites:
//
// 1) Axios installed
//    - run `npm install axios` in current directory
// 2) Docker containers are running
//    - run `docker-compose up --build` in project directory
//
// Run tests:
//    node EndpointsUnitTest.js
//

const globals = require("./globals");
const generalTest = require("./generalTest");
const checkInTest = require("./checkInTest");

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

async function setup() {
  let id = "example";
  let promises = [];

  let endpoint;
  for (endpoint of endpoints) {
    let data = { resourceType: endpoint, id: id };

    let promise = globals.fhirServer.put(`/${endpoint}/${id}`, data);
    promises.push(promise);
  }
  
  await Promise.all(promises).catch((error) => {
    console.log("Setup failed");
    globals.info(error);
  });
}

/**
 * Main test method.
 */
async function main() {
  await setup();

  generalTest().then();
  checkInTest().then();
}

main().then(console.log()).catch(console.error);
