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
  birthDate : string
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
  relationship: enum (CGV Caregiver, SIB, STPCHLD, GUARD Guardian, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
}
*/

exports.genderEnums = {
  Male: "male",
  Female: "female",
  Other: "other",
  "Decline to answer": "unknown"
};

exports.addressUseEnums = {
  Home: "home",
  Temporary: "temp"
};

exports.phoneUseEnums = {
  Home: "home",
  Mobile: "mobile",
  Work: "work"
};

exports.languageEnums = {
  English: "English",
  Spanish: "Spanish"
};

/**
 * Returns the date in YYYY-MM-DD format.
 *
 * @param {Date in MM/DD/YYYY format} date
 */
function parseDate(date) {
  if (!date) return null;
  // Ensure date can be converted into 3 variables
  if (date.split("/").length !== 3) return null;

  const [month, day, year] = date.split("/");
  return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
}

/**
 * Returns the date in MM/DD/YYYY format.
 *
 * @param {Date in YYYY-MM-DD format} date
 */
function prettyDate(date) {
  if (!date) return null;
  // Ensure date can be converted into 3 variables
  if (date.split("-").length !== 3) return null;

  const [year, month, day] = date.split("-");
  return `${month.padStart(2, "0")}/${day.padStart(2, "0")}/${year}`;
}

exports.toFHIR = function (patient) {
  const resource = {
      resourceType: "Patient",
      name: [
          {
              family: patient.family,
              given: [patient.given]
              // middle and suffix are optional
          }
      ],
      telecom: [
          // add later
      ],
      gender: exports.genderEnums[patient.gender],
      birthDate: parseDate(patient.birthDate),
      address: [
          {
              use: exports.addressUseEnums[patient.address.use],
              line: [patient.address.line],
              city: patient.address.city,
              state: patient.address.state,
              postalCode: patient.address.postalCode,
              country: patient.address.country
          }
      ],
      photo: [
          {
              url: "", // add later
              title: "Photo of user"
          }
      ],
      contact: [
          {
              relationship: [
                  {
                      coding: [
                          {
                              system: "http://terminology.hl7.org/CodeSystem/v2-0131",
                              code: "C",
                              display: "Emergency Contact"
                          }
                      ]
                  }
              ],
              name: {
                  family: patient.contact.family,
                  given: [patient.contact.given]
              },
              telecom: [
                  {
                      system: "phone",
                      value: patient.contact.phone.value,
                      use: exports.phoneUseEnums[patient.contact.phone.use]
                  }
              ]
          }
      ],
      communication: [
          {
              language: {
                  text: patient.language
              },
              preferred: true
          }
      ],
      link: [
          {
              other: {
                  reference: patient.link
              }
          }
      ]
  };

  // optional
  if (patient.hasOwnProperty("middle")) {
    resource.name[0].given.push(patient.middle);
  }
  if (patient.hasOwnProperty("suffix")) {
    resource.name[0].suffix = [patient.suffix];
  }
  // add in telecom
  for (let idx in patient.phone) {
    resource.telecom.push({
      system: "phone",
      value: patient.phone[idx].value,
      use: exports.phoneUseEnums[patient.phone[idx].use],
      rank: `${idx}`
    });
  }
  for (let idx in patient.email) {
    resource.telecom.push({
      system: "email",
      value: patient.email[idx],
      rank: `${idx}`
    });
  }

  return resource;
};

exports.toModel = function (patient) {
    const model = {
        resourceType: patient.resourceType,
        id: patient.id,
        family: patient.name ? patient.name[0].family : "",
        given: patient.name
            ? patient.name[0].given
            ? patient.name[0].given[0]
            : ""
            : "",
        middle: patient.name
            ? patient.name[0].given
            ? patient.name[0].given[1]
            : ""
            : "",
        suffix: patient.name
            ? patient.name[0].suffix
            ? patient.name[0].suffix[0]
            : ""
            : "",
        gender: patient.gender,
        birthDate: prettyDate(patient.birthDate),
        address: {
            line: patient.address
                ? patient.address[0].line
                ? patient.address[0].line[0]
                : ""
                : "",
            city: patient.address ? patient.address[0].city : "",
            state: patient.address ? patient.address[0].state : "",
            postalCode: patient.address ? patient.address[0].postalCode : "",
            country: patient.address ? patient.address[0].country : ""
        },
        language: patient.communication
            ? patient.communication[0].language
            ? patient.communication[0].language.text
            : ""
            : "",
        link: patient.link ? patient.link[0].other.reference : null
    };

    return model;
};
