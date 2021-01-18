// Read private configuration settings from .env file into process.env
//
const dotenv = require("dotenv");
dotenv.config();

// Config axios
const axios = require("axios").default;

const fhirUrlBase =
  process.env.FHIR_URL_BASE || "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const instance = axios.create({ baseURL: fhirUrlBase });

instance.defaults.headers.post["Content-Type"] = "application/fhir+json";
instance.defaults.headers.put["Content-Type"] = "application/fhir+json";
instance.defaults.headers.patch["Content-Type"] = "application/json-patch+json";

// Export configuration settings with some values set to defaults.
// NOTE: login/password settings should not be defaulted.
//
module.exports = {
  port: process.env.PORT || 3000,
  axios: instance,
  fhirUrlBase: fhirUrlBase,
  mirthUrl: process.env.MIRTH_URL || "http://localhost:8444/",
};
