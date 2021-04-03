require("dotenv").config({ path: `${__dirname}/../.env` });
const { body, param, validationResult, sanitizeBody } = require("express-validator");
const crypto = require("crypto");

let axios = require("axios").default;
axios.defaults.baseURL = process.env.TIMETAP_BASEURL;

let tt_sessiontoken = "";
if (tt_sessiontoken == "") {
  fetchTimetapSessionToken();
}

async function fetchTimetapSessionToken() {
  // Build our input values
  let timestamp = Date.now();
  let signature = crypto
    .createHash("md5")
    .update(process.env.TIMETAP_APIKEY + process.env.TIMETAP_SECRETKEY)
    .digest("hex");

  // Fetch the new Token
  const res = await axios.get("/sessionToken", {
    params: {
      apiKey: process.env.TIMETAP_APIKEY,
      timestamp: timestamp,
      signature: signature,
    },
  });

  tt_sessiontoken = res.data.sessionToken;
}

exports.createTimetapClient = [
  param("patient", "Validation message here.").isLength({ min: 1, max: 1 }),

  async (req, res) => {
    console.log("Starting createTimetapClient");
    console.log(patient);
    const data = {
      firstName: patient.name.given[0],
      lastName: patient.name.family,
      fullName: patient.name.given[0] + " " + patient.name.family,
      // emailAddress: patient.telecom.wtfbbq,
      dateOfBirth: patient.birthDate,
      sex: patient.gender,
      address1: patient.address[0].line[0],
      address2: patient.address[0].line[1],
      city: patient.address[0].city,
      // county: "foo",
      state: patient.address[0].state,
      zip: patient.address[0].postalCode,
      country: patient.address[0].country,
      timeZone: {},
      allowWaitListText: true,
      homePhone: "555-444-6666",
      cellPhone: "777-333-2222",
      clientIdsClientCanView: [],
      fields: [],
      locale: "en-US",
      tags: [],
    };

    // axios
    //   .post("/clients", data, {
    //     headers: {
    //       Authorization: `Basic ${tt_sessiontoken}`,
    //     },
    //   })
    //   .then((response) => {
    //     console.log("Patient created in TimeTap!");
    //   })
    //   .catch((err) => {
    //     console.log("API request failed, attempting to refresh the token.");
    //     console.log("the magical 'this': " + this);
    //     refreshTimetapSessionToken(this, [patient]);
    //   });
  },
];

async function refreshTimetapSessionToken(callback, ...args) {
  try {
    fetchTimetapSessionToken();
    console.log("Session Token refreshed! " + tt_sessiontoken);
    callback.call(args);
  } catch {
    console.log("Timetap is broken.");
  }
}
