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
  const resource = {
    resourceType: "Encounter",
    status: encounter.status,
    class: {
      system: CLASS_SYSTEM,
      code: "FLD",
      display: "FLD",
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
    subject: {
      reference: encounter.patient,
    },
    serviceProvider: {
      reference: encounter.serviceProvider,
    },
  };

  return resource;
};

exports.toModel = (encounter) => {
  const model = {
    resourceType: encounter.resourceType,
    id: encounter.id,
    status: encounter.status,
  };

  if (encounter.class != null) model.class = encounter.class.code;
  if (encounter.subject != null) model.patient = encounter.subject.reference;
  if (encounter.appointment != null && encounter.appointment[0] != null)
    model.appointment = encounter.appointment[0].reference;
  if (
    encounter.location != null &&
    encounter.location[0] != null &&
    encounter.location[0].location != null
  )
    model.location = encounter.location[0].location.reference;
  if (encounter.period != null) {
    model.start = encounter.period.start;
    model.end = encounter.period.end;
  }

  return model;
};
