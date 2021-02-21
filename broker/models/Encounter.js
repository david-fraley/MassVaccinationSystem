/*
Encounter {
  status : enum (planned, arrived, in-progress, finished, cancelled)
  class : enum (FLD)
  patient : string "Patient/id"
  appointment : string "Appointment/id"
  location : string "Location/id"
}
*/
const CLASS_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
exports.toFHIR = function (encounter) {
  let resource = {
    resourceType: "Encounter",
    status: encounter.status,
    class: {
      system: CLASS_SYSTEM,
      code: "FLD",
      display: "FLD",
    },
    subject: {
      reference: encounter.patient,
    },
    appointment: [
      {
        reference: encounter.appointment,
      },
    ],
    period: { start: new Date().toISOString() },
    location: [
      {
        location: {
          reference: encounter.location,
        },
      },
    ],
    serviceProvider: {
      reference: encounter.serviceProvider,
    },
  };

  return resource;
};

exports.toModel = (encounter) => {
  let model = {
    resourceType: encounter.resourceType,
    id: encounter.id,
    status: encounter.status,
    class: encounter.class.code,
    patient: encounter.subject.reference,
    appointment: encounter.appointment[0].reference,
    location: encounter.location[0].location.reference,
    start: encounter.period.start,
    end: encounter.period.end,
  };

  return model;
};
