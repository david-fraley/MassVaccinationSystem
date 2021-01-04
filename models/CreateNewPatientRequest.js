/*

Patient {

  family : string
  given : string
  --middle : string
  --suffix : string
  phone : [{
    value : string
    use : enum (home, work, temp, old, mobile)
  }]
  email : array of strings
  gender : enum (male, female, other, unknown)
  birthdate : string
  race : enum (American Indian or Alaskan Native, Asian, Native Hawaiian or Other Pacific Islander, Black or African-American, White, Other Race)
  ethnicity: enum (Hispanic, Not Hispanic)
  address: {
    use: enum (home, work, temp)
    line: string
    city: string
    state: string
    postalCode: string
    country: string
  }
  contact: {
    relationship: enum (C Emergency Contact, E Employer, F Federal Agency, I Insurance Company, N Next-of-Kin, S State Agency, U Unknown)
    family: string
    given: string
    phone: {
      value: string
      use: enum (home, work, temp, old, mobile)
    }
  }
  language: enum (English, Spanish)
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
      "address": {
        "use": "home",
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
