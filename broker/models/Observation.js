/*
Observation {
  status : enum (final, amended, corrected)
  category : enum (vital-signs, procedure, exam)
  code : enum (?)
  subject : string "Patient/id"
  encounter : string "Encounter/id"
  value: string
  effectiveStart: string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  effectiveEnd: string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  performer : [string] "(Practitioner/PractitionerRole/Organization)/id"
  partOf : string "Immunization/id"
}
{
  "Observation": {
    "status": "final",
    "category": "procedure",
    "code": "45708-5",
    "subject": "Patient/example",
    "encounter": "Encounter/715",
    "value": "notes",
    "effectiveStart": "2016-05-18T22:33:22Z",
    "effectiveEnd": "2016-05-18T22:38:22Z",
    "performer": [
      "Patient/example"
    ],
    "partOf": "Immunization/1455"
  }
}
*/
const CAT_SYSTEM = "http://hl7.org/fhir/ValueSet/observation-category";
const CODE_SYSTEM = "http://loinc.org";
exports.toFHIR = function (observation) {
  let resource = {
    resourceType: "Observation",
    status: observation.status,
    category: [
      {
        coding: [
          {
            system: CAT_SYSTEM,
            code: observation.category,
            display: observation.category,
          },
        ],
      },
    ],
    code: {
      coding: [
        {
          system: CODE_SYSTEM,
          code: observation.code,
          display: observation.code,
        },
      ],
    },
    subject: {
      reference: observation.subject,
    },
    encounter: {
      reference: observation.encounter,
    },
    valueString: observation.value,
    effectivePeriod: {
      start: observation.effectiveStart,
      end: observation.effectiveEnd,
    },
    performer: [
      // add later
    ],
    partOf: [
      {
        reference: observation.partOf,
      },
    ],
  };

  // add performers
  let performer;
  for (performer of observation.performer) {
    resource.performer.push({
      reference: performer,
    });
  }

  return resource;
}