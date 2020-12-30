/*
Organization {
  active : boolean
  type : enum (prov, bus, govt, edu, reli, cg, other)
  name : string
}
{
  "Organization":
    {
      "active" : true,
      "type" : "cg",
      "name" : "ManagingOrganizationName"
    }
}
*/
const TYPE_SYSTEM = "http://hl7.org/fhir/ValueSet/organization-type";
exports.toFHIR = function (org) {
  let resource = {
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
}