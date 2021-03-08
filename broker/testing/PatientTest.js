// Pre-requisites:
//
// 1) Dependencies installed
//    - run `npm install` in current directory
// 2) Docker containers are running
//    - follow README instructions in project directory
//
// Run tests:
//    npm run test filename.js
//
const assert = require("chai").assert;
const globals = require("./globals");

const options = {
  addressOptions: ["Home", "Temporary"],
  phoneOptions: ["Home", "Mobile", "Work", ""],
  suffixOptions: ["II", "III", "IV", "Jr", "Sr", ""],
  genderOptions: ["Male", "Female", "Other", "Decline to answer"],
  raceOptions: [
    "Black or African American",
    "White",
    "Asian",
    "American Indian or Alaska Native",
    "Native Hawaiian or other Pacific Islander",
    "Other",
  ],
  ethnicityOptions: [
    "Hispanic or Latino",
    "Not Hispanic or Latino",
    "Unknown or prefer not to answer",
  ],
  relationshipOptions: [
    "Spouse",
    "Parent",
    "Guardian",
    "Care Giver",
    "Sibling",
    "Grandparent",
    "Child",
    "Foster Child",
    "Stepchild",
    "Other",
  ],
  languageOptions: ["English", "Spanish"],
};

describe("PatientTest", function () {
  it("Create Patients successfully", async () => {
    let promises = [];
    let triedAllOptions = false;
    let index = 0;

    // shift through all options
    while (!triedAllOptions) {
      let params = []; // list of selected options
      triedAllOptions = true;
      for (const type in options) {
        if (options.hasOwnProperty(type)) {
          // choose the index_th option, or last option if invalid
          const i = Math.min(index, options[type].length - 1);
          const option = options[type][i];
          params.push(option);

          // if any options list has more options to try
          if (index < options[type].length) triedAllOptions = false;
        }
      }
      if (triedAllOptions) break;

      // request to create patient
      const requestBody = globals.examples.PatientTemplate.apply(null, params);
      const data = JSON.parse(requestBody);
      const promise = globals.broker.post("/Patient", data);

      promises.push(promise);

      index++;
    }

    // resolve all requests
    await Promise.all(promises).catch((e) => {
      console.log(e);
      assert(false, "Test failed");
    });
  }).timeout(0); // set no timeout
});
