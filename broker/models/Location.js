/*
Location {
  identifier: WIR client ID
  status : enum (acitve, suspended, inactive)
  name : string
  mode : enum (instance, kind)
  type : enum (HOSP, COMM, MHSP, HU, OUTPHARM, OF, INFD, PREV, CSC, SCHOOL)
  address: {
    line: string
    city: string
    state: string
    postalCode: string
    country: string
  }
  physicalType : enum (Site, Area, Building)
}
{
  "Location":
    { 
      "wirClientID": "1234",
      "status" : "active",
      "name": "LocationName",
      "mode": "instance",
      "type": "COMM",
      "address": {
        "line": "90 Walnut St",
        "city": "New York City",
        "state": "NY",
        "postalCode": "14623",
        "country": "USA"
      },
      "physicalType": "Site"
    }
}
*/

const TYPE_SYSTEM = "http://hl7.org/fhir/ValueSet/location-physical-type";
exports.toFHIR = function (loc) {
  let resource = {
    resourceType: "Location",
    name: loc.name,
    identifier: [
      {
        value: loc.wirClientID,
        type: {
          coding: [
            {
              code: "WIR",
              display: "WIR CLient ID",
            },
          ],
        },
      },
    ],
    status: loc.status,
    mode: loc.mode,
    type: [
      {
        coding: [
          {
            code: loc.type,
            display: loc.type,
          },
        ],
      },
    ],

    address: {
      line: [loc.address.line],
      city: loc.address.city,
      state: loc.address.state,
      postalCode: loc.address.postalCode,
      country: loc.address.country,
    },

    physicalType: {
      coding: [
        {
          system:"http://hl7.org/fhir/ValueSet/location-physical-type",
          code: loc.physicalType,
          display: loc.physicalType,
        },
      ],
    },
  };
  
  return resource;
}
            

