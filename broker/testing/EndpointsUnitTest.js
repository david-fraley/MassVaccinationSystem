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

function config(obj) {
  let status = "";
  try {
    status = obj.status ? obj.status : status;
    status = obj.response ? obj.response.status : status;
  } catch (e) {}

  let message = `${obj.config.method} ${obj.config.url} ${status}\n`;

  console.log(message);
}

// Print the error
function info(e) {
  let message = e.response ? e.response.data : e.message;

  console.log(message);
}

async function setup() {
  let endpoint;
  let id = "example";
  for (endpoint of endpoints) {
    let data = { resourceType: endpoint, id: id };

    fhirServer.put(`/${endpoint}/${id}`, data).then();
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
      .then((response) => config(response))
      .catch((error) => info(error));
    broker
      .post(url, data)
      .then((response) => {
        config(response);
        console.log(JSON.stringify(response.data));
        console.log("\n");
      })
      .catch((error) => info(error));
  }

  // Test Check-in
  let setupAppointment = fhirServer.put(
    "/Appointment/example",
    JSON.parse(ExamplePayloads.CheckInAppointment)
  );
  let setupEncounter = fhirServer.put(
    "/Encounter/example",
    JSON.parse(ExamplePayloads.CheckInEncounter)
  );
  await Promise.all([setupAppointment, setupEncounter]).catch((error) => {
    console.log("Check-in setup failed");
    info(error);
  });

  broker
    .post("/check-in", {}, { params: { appointment: "example" } })
    .then((response) => {
      config(response);
      console.log(JSON.stringify(response.data));
      console.log("\n");
    })
    .catch((error) => info(error));
}

main().then(console.log()).catch(console.error);
