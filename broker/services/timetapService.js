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
  tt_sessiontoken = res.data.sessionToken;
}

exports.createTimetapClient = async (patient) => {
  // Create the Patient record in Timetap
  let phone = "";
  let email = "";
  for (let i = 0; i < patient.telecom.length; i++) {
    if (patient.telecom[i].system === "phone") {
      phone = patient.telecom[i].value;
    }
    if (patient.telecom[i].system === "email") {
      email = patient.telecom[i].value;
    }
  }

  const data = {
    firstName: patient.name[0].given[0],
    lastName: patient.name[0].family,
    fullName: `${patient.name[0].given[0]} ${patient.name[0].family}`,
    emailAddress: email,
    dateOfBirth: patient.birthDate,
    sex: patient.gender,
    address1: patient.address[0].line[0],
    address2: patient.address[0].line[1],
    city: patient.address[0].city,
    state: patient.address[0].state,
    zip: patient.address[0].postalCode,
    country: patient.address[0].country,
    timeZone: {},
    allowWaitListText: true,
    homePhone: phone,
    cellPhone: phone,
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
      // Success!
    })
    .catch(async (err) => {
      // Attempt to refresh our TT token
      await refreshTimetapSessionToken(this, [patient]);
    });
};

async function refreshTimetapSessionToken(callback, args) {
  try {
    await fetchTimetapSessionToken();
    await callback(...args);
  } catch {
    // TimeTap token refresh failed
  }
}
