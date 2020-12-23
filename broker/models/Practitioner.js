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
  "Practitioner":
    {
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
      "qualificationCode": "CER",
   }
}
*/

const TYPE
exports.toFHIR = function (prt) {
  let resource = {
    resourceType: "Practitioner",
    name: [
      {
        family: prt.family,
        given: [prt.given],
        suffix: [prt.suffix],
      },
    ],
    telecom: [
      {
        system: "phone",
        value: prt.phone,
      },
      {
        system: "email",
        value: prt.email,
      },
    ],
    gender: prt.gender,
    birthDate: prt.birthDate,
    address: [
      {
        line: [prt.address.line],
        city: prt.address.city,
        state: prt.address.state,
        postalCode: prt.address.postalCode,
        country: prt.address.country,
      },
    ],
    qualification: [
      {
        code: {
          coding: [
            {
              code: prt.qualificationCode,
              display: prt.QualificationCode,
            },
          ],
        },
      },
    ],
  };
  
  return resource;
}

