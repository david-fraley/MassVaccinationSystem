/*
Organization {
  active : boolean
  type : enum (prov, bus, govt, edu, reli, cg, other)
  name : string
}
*/
const TYPE_SYSTEM = "http://hl7.org/fhir/ValueSet/organization-type";
exports.toFHIR = function (org) {
  const resource = {
    resourceType: "Organization",
    active: org.active,
    type: [
      {
        coding: [
          {
            system: TYPE_SYSTEM,
            code: org.type,
            display: org.type,
          },
        ],
      },
    ],
    name: org.name,
  };

  return resource;
};
