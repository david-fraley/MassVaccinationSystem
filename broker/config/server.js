// Read private configuration settings from .env file into process.env
//
require("dotenv").config({ path: `${__dirname}/../../.env` });
const cors = require('cors');

// Export configuration settings with some values set to defaults.
// NOTE: login/password settings should not be defaulted.
//
let allowedCorsOrigins = null;
if(process.env.CORS_ORIGINS) {
  allowedCorsOrigins = process.env.CORS_ORIGINS.split(',');
}

module.exports = {
  port: process.env.PORT || 3000,
  fhirUrlBase:
    process.env.FHIR_URL_BASE || "http://hapi:8080/hapi-fhir-jpaserver/fhir",
  mirthUrl: process.env.MIRTH_URL || "http://localhost:8444/",

  cors: cors({
    origin: function(origin, callback) {
      
      // If CORS_ORIGINS specified in .env, make sure origin matches one of them
      if(!allowedCorsOrigins || allowedCorsOrigins.indexOf(origin) !== -1) {
        callback(null, true);
      }
      else {
        callback(new Error('Request origin blocked by CORS'));
      }
    }
  })

};
