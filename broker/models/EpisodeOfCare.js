/*
EpisodeOfCare {
  status : enum (planned, active, finished)
  patient : string
  managingOrganization : string
}
*/

exports.toFHIR = function (eoc) {
    const resource = {
        resourceType: "EpisodeOfCare",
        status: eoc.status,
        patient: {
            reference: eoc.patient
        },
        managingOrganization: {
            reference: eoc.managingOrganization
        }
    };
  
    return resource;
}
