/*
RelatedPerson {
  patient: string
  relationship: enum (CGV Caregiver, SIB, STPCHLD, GUARD Guardian, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
}
*/
const RELATIONSHIP_SYSTEM =
  "http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype";

exports.relationshipValueSet = {
  "Care Giver": "CGV",
  Sibling: "SIB",
  Stepchild: "STPCHLD",
  Guardian: "GUARD",
  Child: "CHILD",
  "Foster Child": "CHLDFOST",
  Spouse: "SPS",
  Parent: "PRN",
  Grandparent: "GRPRN",
  Other: "O",
  Self: "ONESELF"
};

exports.toFHIR = function (related) {
  const resource = {
      resourceType: "RelatedPerson",
      patient: {
          reference: related.patient
      },
      relationship: [
          {
              coding: [
                  {
                      system: RELATIONSHIP_SYSTEM,
                      code: exports.relationshipValueSet[related.relationship],
                      display: related.relationship
                  }
              ]
          }
      ]
  };

  return resource;
};
