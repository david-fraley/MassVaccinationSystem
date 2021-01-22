const Appointment = `{
  "Appointment": {
    "status":"booked",
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
}`;

const Encounter = `{
  "Encounter": {
    "status" : "planned",
    "class" : "FLD",
    "subject" : "Patient/example",
    "appointment" : "Appointment/example",
    "start" : "2020-09-17T05:10:19+00:00",
    "end" : "2020-09-18T05:15:19+00:00",
    "location" : "Location/example",
    "serviceProvider" : "Organization/example"
  }
}`;

const EpisodeOfCare = `{
  "EpisodeOfCare": {
    "status" : "planned",
    "patient" : "Patient/example",
    "managingOrganization" : "Organization/example"
  }
}`;

const Immunization = `{
  "Immunization": {
    "vaccine": "FLUVAX",
    "manufacturer": "Organization/example",
    "lotNumber": "AAJN11K",
    "expiration": "2015-02-15",
    "patient": "Patient/example",
    "encounter": "Encounter/example",
    "status": "completed",
    "statusReason": "OSTOCK",
    "occurrence": "2013-01-10",
    "primarySource": true,
    "location": "Location/example",
    "site": "LA",
    "route": "IVINJ",
    "doseQuantity": 5,
    "doseUnit": "mg",
    "performer": ["Practitioner/example"],
    "note": "immunization notes",
    "education": [""],
    "series": "series",
    "doseNumber": 1,
    "seriesDoses": 2
  }
}`;

const Location = `{
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
}`;

const Observation = `{
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
}`;

const Organization = `{
  "Organization": {
    "active" : true,
    "type" : "cg",
    "name" : "ManagingOrganizationName"
  }
}`;

const Patient = `{
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
}`;

const Practitioner = `{
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
}`;

module.exports = {
  Appointment: Appointment,
  Encounter: Encounter,
  EpisodeOfCare: EpisodeOfCare,
  Immunization: Immunization,
  Location: Location,
  Observation: Observation,
  Organization: Organization,
  Patient: Patient,
  Practitioner: Practitioner,
};