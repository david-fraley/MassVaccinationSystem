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

const axios = require("axios").default;
const ExamplePayloads = require("./examples");

const broker = axios.create({
  baseURL: "http://localhost:3000",
});
const fhirServer = axios.create({
  baseURL: "http://localhost:8080/hapi-fhir-jpaserver/fhir",
});

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

function report(obj) {
  let status = "";
  try {
    status = obj.status ? obj.status : status;
    status = obj.response ? obj.response.status : status;
  } catch (e) {}

  let message = `${obj.config.method} ${obj.config.url} ${status}\n`;

  console.log(message);
}

async function setup() {
  let endpoint;
  let id = "example";
  for (endpoint of endpoints) {
    let data = { resourceType: endpoint, id: id };

    fhirServer.put(`/${endpoint}/${id}`, data);
  }
}

/**
 * Main test method.
 */
async function main() {
  await setup();

  let endpoint;
  for (endpoint of endpoints) {
    let url = `/${endpoint}`;
    let data = JSON.parse(ExamplePayloads[endpoint]);

    broker
      .get(url)
      .then((response) => report(response))
      .catch((error) => report(error));
    broker
      .post(url, data)
      .then((response) => {
        report(response);
        console.log(JSON.stringify(response.data));
        console.log("\n");
      })
      .catch((error) => report(error));
  }
}

main().then(console.log()).catch(console.error);
