const globals = require("./globals");

async function setup() {
  let setupAppointment = globals.fhirServer.put(
    "/Appointment/example",
    JSON.parse(globals.examples.CheckInAppointment)
  );
  let setupEncounter = globals.fhirServer.put(
    "/Encounter/example",
    JSON.parse(globals.examples.CheckInEncounter)
  );
  await Promise.all([setupAppointment, setupEncounter]).then().catch((error) => {
    console.log("Check-in setup failed");
    globals.info(error);
  });
}

async function cleanup() {
  setup().then();
}

async function test() {
  globals.broker
    .post("/check-in", {}, { params: { patient: "example" } })
    .then((response) => {
      globals.config(response);
      console.log(JSON.stringify(response.data));
      console.log("\n");
    })
    .catch((error) => globals.info(error));
}

module.exports = async () => {
  await setup();
  await test();
  await cleanup();
};
