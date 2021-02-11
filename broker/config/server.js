// Read private configuration settings from .env file into process.env
//
require("dotenv").config({ path: `${__dirname}/../../.env` });

// Export configuration settings with some values set to defaults.
// NOTE: login/password settings should not be defaulted.
//
module.exports = {
  port: process.env.PORT || 3000,
  fhirUrlBase:
    process.env.FHIR_URL_BASE || "http://hapi:8080/hapi-fhir-jpaserver/fhir",
  mirthUrl: process.env.MIRTH_URL || "http://localhost:8444/",
};
