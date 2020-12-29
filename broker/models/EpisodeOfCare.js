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
      "patient" : "1457642"
      "managingOrganization" : "1703654"
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
