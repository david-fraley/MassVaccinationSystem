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
axios.defaults.baseURL = "http://localhost:3000";

const ExamplePayloads = require("./examples");

function report(obj) {
  let status = "";
  try {
    status = obj.status ? obj.status : status;
    status = obj.response ? obj.response.status : status;
  } catch (e) {}

  let message = `${obj.config.method} ${obj.config.url} ${status}\n`;

  console.log(message);
}

/**
 * Main test method.
 */
async function main() {
  let endpoints = [
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

  let endpoint;
  for (endpoint of endpoints) {
    let url = `/${endpoint}`;
    let data = JSON.parse(ExamplePayloads[endpoint]);

    axios
      .get(url)
      .then((response) => report(response))
      .catch((error) => report(error));
    axios
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
