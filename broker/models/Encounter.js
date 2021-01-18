/*
Encounter {
  status : enum (planned, arrived, in-progress, finished, cancelled)
  class : enum (FLD)
  subject : string "Patient/id"
  appointment : string "Appointment/id"
  start : string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  end : string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
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
      code: encounter.class,
      display: encounter.class,
    },
    subject: {
      reference: encounter.subject,
    },
    appointment: [
      {
        reference: encounter.appointment,
      },
    ],
    period: {
      start: encounter.start,
      end: encounter.end,
    },
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
