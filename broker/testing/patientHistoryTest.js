const globals = require("./globals");

/**
 * Setup for test.
 * Creates example Immunization resources that are linked to Patient/example
 */
async function setup() {
  let notDone = globals.fhirServer.put(
    "/Immunization/examplenotdone",
    JSON.parse(globals.examples.ExampleImmunizationNotDone)
  );
  let completed = globals.fhirServer.put(
    "/Immunization/examplecompleted",
    JSON.parse(globals.examples.ExampleImmunizationCompleted)
  );
  await Promise.all([notDone, completed]).catch((error) => {
    console.log("Patient history setup failed");
    globals.info(error);
  });
}

module.exports = async () => {
  await setup();
};
