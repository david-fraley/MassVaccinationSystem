/**
 * Example request data for endpoint testing
 */

module.exports = {
  Appointment: `{
  "Appointment": {
    "status":"fulfilled",
    "participant":[
      {
        "type":"patient",
        "actor":"Patient/example"
      },
      {
        "type":"pratitioner",
        "actor":"Practitioner/example"
      }
    ],
    "slot":"Slot/example"
  }
}`,

  Encounter: `{
  "Encounter": {
    "status" : "finished",
    "class" : "FLD",
    "subject" : "Patient/example",
    "location" : "Location/example",
    "serviceProvider" : "Organization/example"
  }
}`,

  EpisodeOfCare: `{
  "EpisodeOfCare": {
    "status" : "planned",
    "patient" : "Patient/example",
    "managingOrganization" : "Organization/example"
  }
}`,

  Immunization: `{
  "Immunization": {
    "vaccine": "FLUVAX",
    "manufacturer": "Organization/example",
    "lotNumber": "AAJN11K",
    "expiration": "2015-02-15",
    "patient": "Patient/example",
    "encounter": "Encounter/example",
    "status": "completed",
    "statusReason": "OSTOCK",
    "primarySource": true,
    "location": "Location/example",
    "site": "LA",
    "route": "IVINJ",
    "doseQuantity": "5 mg",
    "performer": ["Practitioner/example"],
    "note": "immunization notes",
    "education": [""],
    "series": "series",
    "doseNumber": 1,
    "seriesDoses": 2
  }
}`,

  Location: `{
  "Location": { 
    "wirClientID": "WIRID",
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
}`,

  Observation: `{
  "Observation": {
    "status": "final",
    "category": "procedure",
    "subject": "Patient/example",
    "encounter": "Encounter/example",
    "note": "notes",
    "effectiveStart": "2016-05-18T22:33:22Z",
    "effectiveEnd": "2016-05-18T22:38:22Z",
    "performer": [
      "Patient/example"
    ],
    "partOf": "Immunization/example"
  }
}`,

  Organization: `{
  "Organization": {
    "active" : true,
    "type" : "cg",
    "name" : "ManagingOrganizationName"
  }
}`,

  ExamplePatient: `{
  "resourceType": "Patient",
  "id": "example",
  "name": [ {
    "family": "Patient",
    "given": [ "Example" ]
  } ],
  "birthDate": "2000-01-01"
}`,

  Patient: `{
  "Patient": [
    {
      "family": "Smith",
      "given": "John",
      "suffix": "Jr.",
      "phone": [
        {
          "value": "(000)000-000",
          "use": "mobile"
        },
        {
          "value": "(100)000-000",
          "use": "mobile"
        }
      ],
      "email": [
        "email@site.com",
        "email2@site.com"
      ],
      "gender": "male",
      "birthDate": "2000-01-01",
      "race": "",
      "ethnicity": "",
      "address": {
        "use": "home",
        "line": "90 Walnut St",
        "city": "New York City",
        "state": "NY",
        "postalCode": "14623",
        "country": "USA"
      },
      "contact": {
        "family": "Smith",
        "given": "John",
        "phone": {
          "value": "(110)000-000",
          "use": "mobile"
        }
      },
      "language": "English",
      "relationship": "ONESELF"
    }
  ]
}`,

  Practitioner: `{
  "Practitioner": {
    "family" : "George",
    "given" : "James",
    "suffix" : "Sr.",
    "phone" : "(010)000-000",
    "email" : "email@site.com",
    "gender": "male",
    "birthDate": "2000-01-01",
    "address": {
      "line": "97 Walnut St",
      "city": "New York City",
      "state": "NY",
      "postalCode": "14623",
      "country": "USA"
    },
    "qualificationCode": "CER"
  }
}`,

  CheckInEncounter: `{
  "resourceType": "Encounter",
  "id": "example",
  "status": "planned",
  "class": {
    "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
    "code": "FLD",
    "display": "FLD"
  },
  "appointment": [ {
    "reference": "Appointment/example"
  } ],
  "subject": {
    "reference": "Patient/example"
  },
  "location": [{ "location": { "reference": "Location/example" } }],
  "serviceProvider": { "reference": "Organization/example" }
}`,

  CheckInAppointment: `{
  "resourceType": "Appointment",
  "id": "example",
  "status": "booked",
  "slot": [{ "reference": "Slot/example" }],
  "participant": [ {
    "actor": {
      "reference": "Patient/example"
    }
  } ]
}`,
};
