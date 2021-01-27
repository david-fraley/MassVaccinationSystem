/**
 * Shared functions/axios instances for use
 * between endpoint tests.
 */

const axios = require("axios").default;
const examples = require("./examples");

const broker = axios.create({
  baseURL: "http://localhost:3000",
});
const fhirServer = axios.create({
  baseURL: "http://localhost:8080/hapi-fhir-jpaserver/fhir",
  headers: {
    post: { "content-type": "application/fhir+json" },
    put: { "content-type": "application/fhir+json" },
    patch: { "content-type": "application/json-patch+json" },
  },
});

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

  config(e);
  console.log(message);
}

module.exports = {
  examples: examples,
  broker: broker,
  fhirServer: fhirServer,
  config: config,
  info: info,
};
