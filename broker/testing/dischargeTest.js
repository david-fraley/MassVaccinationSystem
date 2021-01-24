const globals = require("./globals");

async function setup() {
  //
}

async function cleanup() {
  //
}

async function test() {
  await globals.broker
    .post("/discharge", {}, { params: { appointment: "example" } })
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
