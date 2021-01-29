/*
Immunization {
  vaccine: string
  manufacturer: string "Organization/id"
  lotNumber: string
  expiration: string "YYYY, YYYY-MM, or YYYY-MM-DD"
  patient: string "Patient/id"
  encounter: string "Encounter/id"
  status: enum (completed, entered-in-error, not-done)
  primarySource: boolean
  location: string "Location/id"
  site: enum (LA, RA) i.e. left arm, right arm
  route: enum (IDINJ, IM, NASINHLC, IVINJ, PO, SQ, TRNSDERM) i.e. , inj intramuscular, inhalation nasal, inj intravenous, swallow, inj subcutaneous, transdermal
  doseQuantity: string
  performer: string "{Practitioner/PractitionerRole/Organization}/id"
  note: string
  education: [string] "url",
  series: string,
  doseNumber: int,
  seriesDoses: int
}
*/
const VACC_SYSTEM = "";
const SITE_SYSTEM = "https://www.hl7.org/fhir/v3/ActSite/cs.html";
const ROUTE_SYSTEM =
  "https://www.hl7.org/fhir/v3/RouteOfAdministration/cs.html";
const DOSE_QUANTITY_SYSTEM = "http://unitsofmeasure.org";

const siteEnums = {
  "Left arm": "LA",
  "Right arm": "RA",
};

const routeEnums = {
  "Intradermal injection": "IDINJ",
  "Intramuscular injection": "IM",
  "Nasal inhalation": "NASINHLC",
  "Intravenous injection": "IVINJ",
  "Oral swallow": "PO",
  "Subcutaneous injection": "SQ",
  Transdermal: "TRNSDERM",
};

exports.toFHIR = function (imm) {
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
    manufacturer: {
      reference: imm.manufacturer,
    },
    lotNumber: imm.lotNumber,
    expirationDate: imm.expiration,
    patient: {
      reference: imm.patient,
    },
    encounter: {
      reference: imm.encounter,
    },
    status: imm.status,
    occurrenceDateTime: new Date().toISOString(),
    primarySource: imm.primarySource,
    location: {
      reference: imm.location,
    },
    site: {
      coding: [
        {
          system: SITE_SYSTEM,
          code: siteEnums[imm.site],
          display: imm.site,
        },
      ],
    },
    route: {
      coding: [
        {
          system: ROUTE_SYSTEM,
          code: routeEnums[imm.route],
          display: imm.route,
        },
      ],
    },
    doseQuantity: {
      value: imm.doseQuantity.split(" ")[0],
      system: DOSE_QUANTITY_SYSTEM,
      code: imm.doseQuantity.split(" ")[1],
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
    education: [
      // add later
    ],
    protocolApplied: [
      {
        series: imm.series,
        doseNumberPositiveInt: imm.doseNumber,
        seriesDosesPositiveInt: imm.seriesDoses,
      },
    ],
  };

  // add education
  let education;
  for (education of imm.education) {
    resource.education.push({
      reference: education,
    });
  }

  return resource;
};

exports.toModel = (immunization) => {
  let model;
  try {
    model = {
      vaccine: immunization.vaccineCode.coding[0].code,
      manufacturer: immunization.manufacturer.reference,
      lotNumber: immunization.lotNumber,
      expiration: immunization.expirationDate,
      patient: immunization.patient.reference,
      encounter: immunization.encounter.reference,
      status: immunization.status,
      occurrence: immunization.occurrenceDateTime,
      location: immunization.location.reference,
      site: immunization.site.coding[0].display,
      route: immunization.route.coding[0].display,
      doseQuantity:
        immunization.doseQuantity.value + " " + immunization.doseQuantity.code,
      performer: [immunization.performer[0].actor.reference],
      note: immunization.note ? immunization.note[0].text : immunization.note,
      education: [immunization.education[0].reference],
      series: immunization.protocolApplied[0].series,
      doseNumber: immunization.protocolApplied[0].doseNumberPositiveInt,
      seriesDoses: immunization.protocolApplied[0].seriesDosesPositiveInt,
    };
  } catch (e) {
    console.log(e);
    model = immunization;
  }

  return model;
};
