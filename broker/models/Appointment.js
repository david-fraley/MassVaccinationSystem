/*
Appointment {
  status : enum (proposed, pending, booked, arrived, fulfilled, cancelled, noshow, entered-in-error, checked-in, waitlist)
  participant : [ 
    {
      type : "patient" // note that this type is an extensible
      actor : string "Patient/id"
    },
    {
      type : "practitioner" // note that this type is an extensible
      actor : string "Practitioner/id"
    },
    {
      type : "location" // note that this type is an extensible
      actor : string "Location/id"
    },
  ]
  slot : string "Slot/id" // note that this is not needed until scheduling is implemented
}
*/
exports.toFHIR = function (appt) {
  const resource = {
      resourceType: "Appointment",
      status: appt.status,
      slot: [
          {
              reference: appt.slot
          }
      ],
      participant: [
          // add later
      ]
  };
  // add participants
  let participant;
  for (participant of appt.participant) {
    resource.participant.push({
      type: [
        {
          coding: [
            {
              system: "",
              code: participant.type,
              display: participant.type
            }
          ]
        }
      ],
      actor: {
        reference: participant.actor
      }
    });
  }
  return resource;
};

exports.toModel = (appointment) => {
    const model = {
        resourceType: appointment.resourceType,
        id: appointment.id,
        status: appointment.status,
        participant: appointment.participant.map((x) => {
            return {
                actor: x.actor.reference
            };
        })
    };

    return model;
};
