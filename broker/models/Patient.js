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
    family: string
    given: string
    phone: {
      value: string
      use: enum (home, work, temp, old, mobile)
    }
  }
  language: enum (English, Spanish),
  relationship: enum (DOMPART Domestic Partner, INLAW In-Law, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
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
}

*/
exports.toFHIR = function (patient) {
  let resource = {
    resourceType: "Patient",
    name: [
      {
        family: patient.family,
        given: [patient.given],
        // middle and suffix are optional
      },
    ],
    telecom: [
      // add later
    ],
    gender: patient.gender,
    birthDate: patient.birthDate,
    address: [
      {
        use: patient.address.use,
        line: [patient.address.line],
        city: patient.address.city,
        state: patient.address.state,
        postalCode: patient.address.postalCode,
        country: patient.address.country,
      },
    ],
    photo: [
      {
        url: "", // add later
        title: "Photo of user",
      },
    ],
    contact: [
      {
        relationship: [
          {
            coding: [
              {
                system: "http://terminology.hl7.org/CodeSystem/v2-0131",
                code: "C",
                display: "Emergency Contact",
              },
            ],
          },
        ],
        name: {
          family: patient.contact.family,
          given: [patient.contact.given],
        },
        telecom: [
          {
            system: "phone",
            value: patient.contact.phone.value,
            use: patient.contact.phone.use,
          },
        ],
      },
    ],
    communication: [
      {
        language: {
          text: patient.language,
        },
        preferred: true,
      },
    ],
    link: [
      // add link
    ],
  };

  // optional
  if (patient.hasOwnProperty("middle")) {
    resource.name[0].given.push(patient.middle);
  }
  if (patient.hasOwnProperty("suffix")) {
    resource.name[0].suffix = [patient.suffix];
  }
  // add in telecom
  for (idx in patient.phone) {
    resource.telecom.push({
      system: "phone",
      value: patient.phone[idx].value,
      use: patient.phone[idx].use,
      rank: `${idx}`,
    });
  }
  for (idx in patient.email) {
    resource.telecom.push({
      system: "email",
      value: patient.email[idx],
      rank: `${idx}`,
    });
  }
  // add link
  for (link of patient.link) {
    resource.link.push({
      other: {
        reference: link,
      },
    });
  }

  return resource;
};

exports.toModel = function (patient) {
  let model = {
    id: patient.id,
    family: patient.name ? patient.name[0].family : "",
    given: patient.name
      ? patient.name[0].given
        ? patient.name[0].given[0]
        : ""
      : "",
    birthDate: patient.birthDate,
    address: {
      line: patient.address
        ? patient.address[0].line
          ? patient.address[0].line[0]
          : ""
        : "",
      city: patient.address ? patient.address[0].city : "",
      state: patient.address ? patient.address[0].state : "",
      postalCode: patient.address ? patient.address[0].postalCode : "",
      country: patient.address ? patient.address[0].country : "",
    },
  };

  return model;
};
