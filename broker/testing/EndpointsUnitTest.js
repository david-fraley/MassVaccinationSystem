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

/**
 * Setup for all tests.
 * Creates referenced resources.
 */
async function setup() {
  const resources = [
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

  let id = "example";
  let promises = [];

  let resource;
  for (resource of resources) {
    let data = { resourceType: resource, id: id };
    if (resource === "Patient") data = globals.examples.ExamplePatient;

    let promise = globals.fhirServer.put(`/${resource}/${id}`, data);
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

  // Run tests
  generalTest().then();
  checkInTest().then();
}

main().then(console.log()).catch(console.error);
