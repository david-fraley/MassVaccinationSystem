/*
Location {
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
  "Location": [
    {
      "status" : "active"
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
  ]
}
*/
