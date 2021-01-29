/*
Immunization {
  vaccine: string
  patient: string "Patient/id"
  encounter: string "Encounter/id"
  status: enum (completed, entered-in-error, not-done)
  reason: enum (IMMUNE, MEDPREC, OSTOCK, PATOBJ) i.e. immunity, medical precaution, product out of stock, patient objection
  primarySource: boolean
  location: string "Location/id"
  performer: [string] "{Practitioner/PractitionerRole/Organization}/id"
  note: string
}
*/
const VACC_SYSTEM = "";
const STAT_REASON_SYSTEM = "https://www.hl7.org/fhir/v3/ActReason/cs.html";

const reasonEnums = {
  Immunity: "IMMUNE",
  "Medical precaution": "MEDPREC",
  "Product out of stock": "OSTOCK",
  "Patient objection": "PATOBJ",
};

exports.toFHIR = (imm) => {
  let resource = {
    resourceType: "Immunization",
    vaccineCode: {
      coding: [
        {
          system: VACC_SYSTEM,
          code: imm.vaccine,
          display: imm.vaccine,
        },
      ],
    },
    patient: {
      reference: imm.patient,
    },
    encounter: {
      reference: imm.encounter,
    },
    status: imm.status,
    statusReason: {
      coding: [
        {
          system: STAT_REASON_SYSTEM,
          code: reasonEnums[imm.reason],
          display: imm.reason,
        },
      ],
    },
    occurrenceDateTime: new Date().toISOString(),
    primarySource: imm.primarySource,
    location: {
      reference: imm.location,
    },
    performer: [
      // add later
    ],
    note: [
      {
        text: imm.note,
      },
    ],
  };

  // add performer
  let performer;
  for (performer of imm.performer) {
    resource.performer.push({
      actor: {
        reference: performer,
      },
    });
  }

  return resource;
};
