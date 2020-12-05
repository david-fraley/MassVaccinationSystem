/*

Patient {

  family : string
  given : string
  suffix : string
  phone : {
    value : string
    use : enum (home, work, temp, old, mobile)
  }
  email : array of strings
  gender : enum (male, female, other, unknown)
  birthdate : date
  race : enum (American Indian or Alaskan Native, Asian, Native Hawaiian or Other Pacific Islander, Black or African-American, White, Other Race)
  ethnicity: Hispanic, Not Hispanic
  permissionToFollowup: boolean
  address: {
    
  }
}

{
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
      "follow-up": "",
      "address": {
        "line": "90 Walnut St",
        "city": "New York City",
        "state": "NY",
        "postalCode": "14623",
        "country": "USA"
      },
      "contact": {
        "relationship": "C",
        "family": "Smith",
        "given": "John",
        "phone": {
          "value": "(110)000-000",
          "use": "mobile"
        }
      },
      "language": "English"
    }
  ]
}

*/
