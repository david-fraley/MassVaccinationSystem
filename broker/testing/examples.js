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
    "patient" : "Patient/example",
    "appointment" : "Appointment/example",
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

  ExampleImmunizationCompleted: `{
  "resourceType": "Immunization",
  "id": "examplecompleted",
  "status": "completed",
  "vaccineCode": {
    "coding": [ {
      "code": "COVID",
      "display": "COVID"
    } ]
  },
  "patient": {
    "reference": "Patient/example"
  },
  "encounter": {
    "reference": "Encounter/example"
  },
  "occurrenceDateTime": "2021-02-09T20:57:11.046Z",
  "primarySource": true,
  "location": {
    "reference": "Location/example"
  },
  "manufacturer": {
    "reference": "Organization/example"
  },
  "lotNumber": "AAJN11K",
  "expirationDate": "2015-02-15",
  "site": {
    "coding": [ {
      "system": "https://www.hl7.org/fhir/v3/ActSite/cs.html",
      "code": "LA",
      "display": "Left arm"
    } ]
  },
  "route": {
    "coding": [ {
      "system": "https://www.hl7.org/fhir/v3/RouteOfAdministration/cs.html",
      "code": "IM",
      "display": "Intramuscular injection"
    } ]
  },
  "doseQuantity": {
    "value": 5,
    "system": "http://unitsofmeasure.org",
    "code": "mg"
  },
  "performer": [ {
    "actor": {
      "reference": "Practitioner/example"
    }
  } ],
  "note": [ {
    "text": "immunization notes"
  } ],
  "education": [ {
    "reference": "faq"
  } ],
  "protocolApplied": [ {
    "series": "series",
    "doseNumberPositiveInt": 1,
    "seriesDosesPositiveInt": 2
  } ]
}`,

  ExampleImmunizationNotDone: `{
  "resourceType": "Immunization",
  "id": "examplenotdone",
  "status": "not-done",
  "statusReason": {
    "coding": [ {
      "system": "https://www.hl7.org/fhir/v3/ActReason/cs.html",
      "code": "MEDPREC",
      "display": "Medical Precaution"
    } ]
  },
  "vaccineCode": {
    "coding": [ {
      "code": "COVID",
      "display": "COVID"
    } ]
  },
  "patient": {
    "reference": "Patient/example"
  },
  "encounter": {
    "reference": "Encounter/example"
  },
  "occurrenceDateTime": "2021-02-09T20:52:46.437Z",
  "location": {
    "reference": "Location/example"
  },
  "performer": [ {
    "actor": {
      "reference": "Practitioner/example"
    }
  } ],
  "note": [ {
    "text": "immunization notes"
  } ]
}`,

  Immunization: `{
  "Immunization": {
    "vaccine": "FLUVAX",
    "manufacturer": "Organization/example",
    "lotNumber": "AAJN11K",
    "expirationDate": "2015-02-15",
    "patient": "Patient/example",
    "encounter": "Encounter/example",
    "status": "completed",
    "primarySource": true,
    "location": "Location/example",
    "site": "Left arm",
    "route": "Intramuscular injection",
    "doseQuantity": "5 mg",
    "performer": "Practitioner/example",
    "note": "immunization notes",
    "education": ["faq"],
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
    "family": "Bush",
    "given": [ "Joe" ]
  } ],
  "birthDate": "1999-11-11"
}`,

  Patient: `{
  "Patient": [
    {
      "family": "Smith",
      "given": "John",
      "suffix": "Jr.",
      "phone": [
        {
          "value": "(200)000-0000",
          "use": "Mobile"
        },
        {
          "value": "(100)000-0000",
          "use": "Mobile"
        }
      ],
      "email": [
        "email@site.com",
        "email2@site.com"
      ],
      "gender": "Male",
      "birthDate": "01/01/2000",
      "race": "White",
      "ethnicity": "Not Hispanic or Latino",
      "address": {
        "use": "Home",
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
          "value": "(110)000-0000",
          "use": "Mobile"
        }
      },
      "language": "English",
      "relationship": "Self"
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
