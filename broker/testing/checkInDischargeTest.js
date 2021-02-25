const globals = require("./globals");

/**
 * Setup for check-in test.
 * Creates Appointment resource.
 */
async function setup() {
  let setupAppointment = globals.fhirServer.put(
    "/Appointment/example",
    JSON.parse(globals.examples.CheckInAppointment)
  );
  await Promise.all([setupAppointment]).catch((error) => {
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
 * Send request to discharge endpoint.
 */
async function discharge(appointment, encounter) {
  await globals.broker
    .post(
      "/discharge",
      {},
      { params: { appointment: appointment, encounter: encounter } }
    )
    .then((response) => {
      globals.config(response);
      console.log(JSON.stringify(response.data));
      console.log("\n");
    })
    .catch((error) => globals.info(error));
}

/**
 * Send request to check-in endpoint.
 */
async function checkIn() {
  return await globals.broker
    .post("/check-in", {
      status: "arrived",
      patient: "Patient/example",
      location: "Location/example"
    })
    .then((response) => {
      globals.config(response);
      console.log(JSON.stringify(response.data));
      console.log("\n");

      let appointment = response.data.Appointment.id;
      let encounter = response.data.Encounter.id;
      return { appointment: appointment, encounter: encounter };
    })
    .catch((error) => globals.info(error));
}

/**
 * Check-in and then discharge.
 */
async function test() {
  let response = await checkIn();

  await discharge(response.appointment, response.encounter);
}

module.exports = async () => {
  await setup();
  await test();
  await cleanup();
};
