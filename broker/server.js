// Load configuration settings
const configs = require("./config/server.js");

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


// var bodyParser = require('body-parser');
// var cors = require('cors');
// app.use(bodyParser.json());
// // Enable CORS support
// app.use(cors());

var session = require('express-session');
var Keycloak = require('keycloak-connect');
var memoryStore = new session.MemoryStore();

const app = express();
app.use(express.json());
app.set("json spaces", 2);

var kcConfig = {clientId: "massvaxx", 
bearerOnly: true, 
serverUrl: "https://massvaxx-keycloak.mooo.com/auth/", 
realm: "massVaxx"};

app.use(session({
  secret: 'some secret',
  resave: false,
  saveUninitialized: true,
  store: memoryStore
}));

var keycloak = new Keycloak({
  store: memoryStore
}, kcConfig);

//look over and update below, the next commented out section is from the example

// app.use(keycloak.middleware({
//   logout: '/logout',
//   admin: '/'
// }));

// app.get('/service/public', function (req, res) {
//   res.json({message: 'public'});
// });

// app.get('/service/secured', keycloak.protect('realm:user'), function (req, res) {
//   res.json({message: 'secured'});
// });

// app.get('/service/admin', keycloak.protect('realm:admin'), function (req, res) {
//   res.json({message: 'admin'});
// });

// app.use('*', function (req, res) {
//   res.send('Not found!');
// });

// app.listen(3000, function () {
//   console.log('Started at port 3000');
// });

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
app.get("/Patient/:qrCode", Patient.read);

// POST request since client is sending PHI
app.post("/SearchPatients", Patient.search);

app.post("/Immunization", Immunization.create);
app.get("/Immunization*", Immunization.read);

app.post("/Appointment", Appointment.create);
app.get("/Appointment*", Appointment.read);

app.post("/Organization", Organization.create);
app.get("/Organization*", Organization.read);

app.post("/Observation", Observation.create);
app.get("/Observation*", Observation.read);

// Tell Mirth to send an HL7 message
app.post("/SendHL7Message", SendHL7Message);

app.post("/Encounter", Encounter.post);
app.get("/Encounter*", Encounter.read);

app.post("/EpisodeOfCare", EpisodeOfCare.create);
app.get("/EpisodeOfCare*", EpisodeOfCare.read);

app.post("/Practitioner", Practitioner.create);
app.get("/Practitioner*", Practitioner.read);

app.post("/Location", Location.create);
app.get("/Location*", Location.read);

app.post("/check-in", CheckIn);

app.post("/discharge", Discharge);

app.listen(configs.port, () =>
  console.log(`Listening on port ${configs.port}`)
);
