const configs = require("../config/server.js");

// Config axios
const axios = require("axios").default;

axios.defaults.baseURL = configs.fhirUrlBase;
axios.defaults.headers.post["content-type"] = "application/fhir+json";
axios.defaults.headers.put["content-type"] = "application/fhir+json";
axios.defaults.headers.patch["content-type"] = "application/json-patch+json";

module.exports = axios;
