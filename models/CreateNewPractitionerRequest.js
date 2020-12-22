/*
Practitioner {
  family : string
  given : string
  suffix : string
  phone : string
  email : string
  gender : enum (male, female, other, unknown)
  birthdate : date
  address : {
    line: string
    city: string
    state: string
    postalCode: string
    country: string
  }
  qualificationCode : enum (BN, CANP, CER, CMA, CNP, CNS, CRN, EMT, RN, RMA, PN, RPH)
  }
}
{
  "Practitioner": [
    {
      "family" : "George",
      "given" : "James",
      "suffix" : "Sr.",
      "phone" : "(010)000-000"
      "email" : "email@site.com"
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
  ]
}
*/
