const globals = require("./globals");
const dischargeTest = require("./dischargeTest");

/**
 * Setup for check-in test.
 * Creates Appointment and Encounter resources to update.
 */
async function setup() {
  let setupAppointment = globals.fhirServer.put(
    "/Appointment/example",
    JSON.parse(globals.examples.CheckInAppointment)
  );
  let setupEncounter = globals.fhirServer.put(
    "/Encounter/example",
    JSON.parse(globals.examples.CheckInEncounter)
  );
  await Promise.all([setupAppointment, setupEncounter]).catch((error) => {
    console.log("Check-in setup failed");
    globals.info(error);
  });
}

/**
 * Reset Appointment and Encounter resources for testing by POD.
 */
async function cleanup() {
  await setup();
}

/**
 * Send request to check-in endpoint.
 */
async function test() {
  await globals.broker
    .post("/check-in", {}, { params: { patient: "example" } })
    .then((response) => {
      globals.config(response);
      console.log(JSON.stringify(response.data));
      console.log("\n");
    })
    .catch((error) => globals.info(error));
  await dischargeTest();
}

module.exports = async () => {
  await setup();
  await test();
  //await cleanup();
};
