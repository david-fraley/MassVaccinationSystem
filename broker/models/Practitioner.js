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
*/

exports.toFHIR = function (prt) {
    const resource = {
        resourceType: "Practitioner",
        name: [
            {
                family: prt.family,
                given: [prt.given],
                suffix: [prt.suffix]
            }
        ],
        telecom: [
            {
                system: "phone",
                value: prt.phone
            },
            {
                system: "email",
                value: prt.email
            }
        ],
        gender: prt.gender,
        birthDate: prt.birthDate,
        address: [
            {
                line: [prt.address.line],
                city: prt.address.city,
                state: prt.address.state,
                postalCode: prt.address.postalCode,
                country: prt.address.country
            }
        ],
        qualification: [
            {
                code: {
                    coding: [
                        {
                            code: prt.qualificationCode,
                            display: prt.QualificationCode
                        }
                    ]
                }
            }
        ]
    };
  
    return resource;
}

