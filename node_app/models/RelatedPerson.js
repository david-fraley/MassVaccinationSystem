/*
RelatedPerson {
  patient: string
  relationship: enum (DOMPART Domestic Partner, INLAW In-Law, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
}
*/
const RELATIONSHIP_SYSTEM =
  "http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype";
exports.toFHIR = function (related) {
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
            code: related.relationship,
            display: related.relationship,
          },
        ],
      },
    ],
  };

  return resource;
};
