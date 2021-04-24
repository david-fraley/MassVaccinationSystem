// Load configuration settings
const configs = require("./config/server.js");
const keycloakConfig = require("./config/keycloakConfig.js");
const keycloak = keycloakConfig.keycloak;

const express = require("express");
const Patient = require("./endpoints/Patient");
const Organization = require("./endpoints/Organization");
const Encounter = require("./endpoints/Encounter");
const Observation = require("./endpoints/Observation");
const Appointment = require("./endpoints/Appointment");
const Immunization = require("./endpoints/Immunization");
const EpisodeOfCare = require("./endpoints/EpisodeOfCare");
const Practitioner = require("./endpoints/Practitioner");
const Location = require("./endpoints/Location");
const SendHL7Message = require("./endpoints/SendHL7Message");
const CheckIn = require("./endpoints/CheckIn");
const Discharge = require("./endpoints/Discharge");

const app = express();

app.use(keycloakConfig.session);

app.use(express.json());
app.set("json spaces", 2);
app.use(keycloak.middleware());

// Verify that requests can only be made from whitelisted domains 
// configurable - see .env.template in root directory
app.use(configs.cors);

if (process.env.DEVELOPMENT == 1) {
  const reg_path = __dirname + "/PatientRegistrationViews/";
  const pod_path = __dirname + "/PointOfDispensingViews/";

  app.use("/Registration", express.static(reg_path));
  app.use("/POD", express.static(pod_path));
}

app.all("/broker/*", (req, res) => {
  req.url = req.url.replace(/^(\/broker)/, "");
  app.handle(req, res);
});

//Health check endpoint
app.get("/healthcheck", (req, res) => {
  res.send("Success!");
});

app.post("/Patient", Patient.create);
app.get("/Patient/:qrCode", keycloak.protect(), Patient.read);
app.put("/Patient/:id", keycloak.protect(), Patient.update);

// POST request since client is sending PHI
app.post("/SearchPatients", keycloak.protect(), Patient.search);

app.post("/Immunization", keycloak.protect(), Immunization.create);
app.get("/Immunization*", keycloak.protect(), Immunization.read);

app.post("/Appointment", keycloak.protect(), Appointment.create);
app.get("/Appointment*", keycloak.protect(), Appointment.read);

app.post("/Organization", keycloak.protect(), Organization.create);
app.get("/Organization*", keycloak.protect(), Organization.read);

app.post("/Observation", keycloak.protect(), Observation.create);
app.get("/Observation*", keycloak.protect(), Observation.read);

// Tell Mirth to send an HL7 message
app.post("/SendHL7Message", keycloak.protect(), SendHL7Message);

app.post("/Encounter", keycloak.protect(), Encounter.post);
app.get("/Encounter*", keycloak.protect(), Encounter.read);

app.post("/EpisodeOfCare", keycloak.protect(), EpisodeOfCare.create);
app.get("/EpisodeOfCare*", keycloak.protect(), EpisodeOfCare.read);

app.post("/Practitioner", keycloak.protect(), Practitioner.create);
app.get("/Practitioner*", keycloak.protect(), Practitioner.read);

app.post("/Location", keycloak.protect(), Location.create);
app.get("/Location*", keycloak.protect(), Location.read);

app.post("/check-in", keycloak.protect(), CheckIn);

app.post("/discharge", keycloak.protect(), Discharge);

// Catch-all error handler, currently only used to catch errors thrown by CORS middleware
app.use((err, req, res, next) => {
  if(err) {
    if(typeof err.message === 'string') {
      res.status(500).json({error: err.message});
    }
    else {
      res.status(500).json({error: err});
    }
  }
})

app.listen(configs.port, () =>
  console.log(`Listening on port ${configs.port}`)
);
