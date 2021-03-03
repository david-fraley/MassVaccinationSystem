/*
Immunization {
  vaccine: string
  patient: string "Patient/id"
  encounter: string "Encounter/id"
  status: enum (completed, entered-in-error, not-done)
  reason: enum (IMMUNE, MEDPREC, OSTOCK, PATOBJ) i.e. immunity, medical precaution, product out of stock, patient objection
  primarySource: boolean
  location: string "Location/id"
  performer: string "{Practitioner/PractitionerRole/Organization}/id"
  note: string
}
*/
const VACC_SYSTEM = "";
const STAT_REASON_SYSTEM = "https://www.hl7.org/fhir/v3/ActReason/cs.html";

const reasonEnums = {
  Immunity: "IMMUNE",
  "Medical Precaution": "MEDPREC",
  "Out of Stock": "OSTOCK",
  "Patient Objection": "PATOBJ",
};

exports.toFHIR = (imm) => {
  const resource = {
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
      {
        actor: {
          reference: imm.performer,
        },
      },
    ],
    note: [
      {
        text: imm.note,
      },
    ],
  };

  return resource;
};

exports.toModel = (immunization) => {
  let model;
  try {
    model = {
      id: immunization.id,
      vaccine: immunization.vaccineCode.coding[0].display,
      patient: immunization.patient.reference,
      encounter: immunization.encounter.reference,
      status: immunization.status,
      reason: immunization.statusReason.coding[0].display,
      occurrence: immunization.occurrenceDateTime,
      location: immunization.location.reference,
      performer: [immunization.performer[0].actor.reference],
      note: immunization.note ? immunization.note[0].text : immunization.note,
    };
  } catch (e) {
    model = immunization;
  }

  return model;
};
