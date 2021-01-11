/*
EpisodeOfCare {
  status : enum (planned, active, finished)
  patient : string
  managingOrganization : string
}
{
  "EpisodeOfCare":
    {
      "status" : "planned"
      "patient" : "Patient/example"
      "managingOrganization" : "Organization/hl7"
     }
}
*/

exports.toFHIR = function (eoc) {
  let resource = {
    resourceType: "EpisodeOfCare",
    status: eoc.status,
    patient: {
      reference: eoc.patient,
    },
    managingOrganization: {
      reference: eoc.managingOrganization,
    },
  };
  
  return resource;
}
