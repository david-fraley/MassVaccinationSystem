const ImmunizationCompleted = require("../models/ImmunizationCompleted");
const ImmunizationNotDone = require("../models/ImmunizationNotDone");

exports.toFHIR = (immunization) => {
  let resource;

  switch (immunization.status) {
    case "completed":
      resource = ImmunizationCompleted.toFHIR(immunization);
      break;
    case "not-done":
      resource = ImmunizationNotDone.toFHIR(immunization);
      break;
    default:
      throw "Invalid Immunization model";
  }

  return resource;
};

exports.toModel = (immunization) => {
  let model;

  switch (immunization.status) {
    case "completed":
      model = ImmunizationCompleted.toModel(immunization);
      break;
    case "not-done":
      model = ImmunizationNotDone.toModel(immunization);
      break;
    default:
      model = immunization;
  }

  return model;
};
