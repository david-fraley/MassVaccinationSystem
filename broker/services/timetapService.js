require("dotenv").config({ path: `${__dirname}/../.env` });
const { body, param, validationResult, sanitizeBody } = require("express-validator");
const crypto = require("crypto");

const axios = require("axios");
const timetapAxios = axios.create({
  baseURL: process.env.TIMETAP_BASEURL
})

let tt_sessiontoken = "";
if (tt_sessiontoken == "") {
  try {
    fetchTimetapSessionToken();
  } catch (error) {
    console.log(error);
  }

}

async function fetchTimetapSessionToken() {
  // Build our input values
  let timestamp = Date.now();
  let signature = crypto
    .createHash("md5")
    .update(process.env.TIMETAP_APIKEY + process.env.TIMETAP_SECRETKEY)
    .digest("hex");

  // Fetch the new Token
  const res = await timetapAxios.get("/sessionToken", {
    params: {
      apiKey: process.env.TIMETAP_APIKEY,
      timestamp: timestamp,
      signature: signature,
    },
  });
  console.log(res.data.sessionToken);
  tt_sessiontoken = res.data.sessionToken;
}

exports.createTimetapClient = async (patient) => {
  console.log("Starting createTimetapClient");
  console.log(patient);

  const data = {
    firstName: patient.name[0].given[0],
    lastName: patient.name[0].family,
    fullName: `${patient.name[0].given[0]} ${patient.name[0].family}`,
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
  
  await timetapAxios
    .post("/clients", data, {
      headers: {
        Authorization: `Bearer ${tt_sessiontoken}`,
      },
    })
    .then((response) => {
      console.log("Patient created in TimeTap!");
    })
    .catch(async (err) => {
      console.log("API request failed, attempting to refresh the token.");
      console.log("the magical 'this': " + this);
      await refreshTimetapSessionToken(this, [patient]);
    });
};

async function refreshTimetapSessionToken(callback, args) {
  try {
    await fetchTimetapSessionToken();
    console.log("Session Token refreshed! " + tt_sessiontoken);
    await callback(...args);
  } catch {
    console.log("Timetap is broken.");
  }
}
