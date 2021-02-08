/*
RelatedPerson {
  patient: string
  relationship: enum (CARE Caregiver, SIB, STPCHLD, G Guardian, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
}
*/
const RELATIONSHIP_SYSTEM =
  "http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype";

let relationshipEnums = {
  "Care Giver": "CARE",
  Sibling: "SIB",
  Stepchild: "STPCHLD",
  Guardian: "G",
  Child: "CHILD",
  "Foster Child": "CHLDFOST",
  Spouse: "SPS",
  Parent: "PRN",
  Grandparent: "GRPRN",
  Other: "O",
  Self: "ONESELF",
};

exports.toFHIR = function (related) {
  if (!related.relationship) related.relationship = "Self";

  let resource = {
    resourceType: "RelatedPerson",
    patient: {
      reference: related.patient,
    },
    relationship: [
      {
        coding: [
          {
            system: RELATIONSHIP_SYSTEM,
            code: relationshipEnums[related.relationship],
            display: related.relationship,
          },
        ],
      },
    ],
  };

  return resource;
};
