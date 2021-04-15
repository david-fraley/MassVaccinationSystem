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
    relationship: enum (CGV Caregiver, SIB, STPCHLD, GUARD Guardian, CHILD Child, CHLDFOST Foster Child, SPS Spouse, PRN Parent, GRPRN Grandparent, O Other, ONESELF)
    phone: {
      value: string
      use: enum (home, work, temp, old, mobile)
    }
  }
  language: enum (English, Spanish),
  
}
*/

exports.genderEnums = {
  Male: "male",
  Female: "female",
  Other: "other",
  "Decline to answer": "unknown",
  male: "Male",
  female: "Female",
  other: "Other",
  unknown: "Decline to answer"
};

exports.addressUseEnums = {
  Home: "home",
  Temporary: "temp",
  home: "Home",
  temp: "Temporary"
};

exports.phoneUseEnums = {
  Home: "home",
  Mobile: "mobile",
  Work: "work",
  home: "Home",
  mobile: "Mobile",
  work: "Work"
};

exports.languageEnums = {
  English: "English",
  Spanish: "Spanish"
}

exports.raceValueSet = {
  "American Indian or Alaska Native": {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "1002-5",
    display: "American Indian or Alaska Native",
  },
  Asian: {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2028-9",
    display: "Asian",
  },
  "Black or African American": {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2054-5",
    display: "Black or African American",
  },
  "Native Hawaiian or Other Pacific Islander": {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2076-8",
    display: "Native Hawaiian or Other Pacific Islander",
  },
  White: {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2106-3",
    display: "White",
  },
  Other: {
    system: "http://terminology.hl7.org/CodeSystem/v3-NullFlavor",
    code: "UNC",
    display: "un-encoded",
  },
};

exports.ethnicityValueSet = {
  "Hispanic or Latino": {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2135-2",
    display: "Hispanic or Latino",
  },
  "Not Hispanic or Latino": {
    system: "urn:oid:2.16.840.1.113883.6.238",
    code: "2186-5",
    display: "Non Hispanic or Latino",
  },
};

exports.relationshipValueSet = {
  "Care Giver": "CGV",
  Sibling: "SIB",
  Stepchild: "STPCHLD",
  Guardian: "GUARD",
  Child: "CHILD",
  "Foster Child": "CHLDFOST",
  Spouse: "SPS",
  Parent: "PRN",
  Grandparent: "GRPRN",
  Other: "O",
  Self: "ONESELF"
};

const RELATIONSHIP_SYSTEM =
  "http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype";

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
  if (!date) return undefined;
  // Ensure date can be converted into 3 variables
  if (date.split("-").length !== 3) return undefined;

  const [year, month, day] = date.split("-");
  return `${month.padStart(2, "0")}/${day.padStart(2, "0")}/${year}`;
}

exports.toFHIR = function (patient) {
  const resource = {
      resourceType: "Patient",
      id: patient.id,
      identifier: patient.identifier,
      extension: [],
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
                              system: RELATIONSHIP_SYSTEM,
                              code: exports.relationshipValueSet[patient.contact.relationship],
                              display: patient.contact.relationship
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
  if (patient.address.line2) resource.address[0].line.push(patient.address.line2);
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
  // race
  const race = {
    url: "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
    extension: [],
  };
  // handle unknown race values
  // if (exports.raceValueSet.hasOwnProperty(patient.race))
    race.extension.push({
      url: "ombCategory",
      valueCoding: {
        system: exports.raceValueSet[patient.race].system,
        code: exports.raceValueSet[patient.race].code,
        display: exports.raceValueSet[patient.race].display,
      },
    });
  // handle unknown race values
  // else race.extension.push({ url: "text", valueString: patient.race });

  resource.extension.push(race);
  // ethnicity
  const ethnicity = {
    url: "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity",
    extension: [],
  };
  if (exports.ethnicityValueSet.hasOwnProperty(patient.ethnicity))
    ethnicity.extension.push({
      url: "ombCategory",
      valueCoding: {
        system: exports.ethnicityValueSet[patient.ethnicity].system,
        code: exports.ethnicityValueSet[patient.ethnicity].code,
        display: exports.ethnicityValueSet[patient.ethnicity].display,
      },
    });
  else ethnicity.extension.push({ url: "text", valueString: patient.ethnicity });

  resource.extension.push(ethnicity);

  return resource;
};

exports.toModel = function (patient) {
    const model = {
        resourceType: patient.resourceType,
        id: patient.id,
        identifier: patient.identifier,
        family: (()=>{try{return patient.name[0].family;}catch(e){return undefined;}})(),
        given: (()=>{try{return patient.name[0].given[0];}catch(e){return undefined;}})(),
        middle: (()=>{try{return patient.name[0].given[1];}catch(e){return undefined;}})(),
        suffix: (()=>{try{return patient.name[0].suffix[0];}catch(e){return undefined;}})(),
        phone: (()=>{try{return patient.telecom.filter(obj => obj.system === "phone").map(obj => {return {value: obj.value, use: exports.phoneUseEnums[obj.use]}});}catch(e){return [];}})(),
        email: (()=>{try{return patient.telecom.filter(obj => obj.system === "email").map(obj => obj.value);}catch(e){return [];}})(),
        gender: exports.genderEnums[patient.gender],
        birthDate: prettyDate(patient.birthDate),
        race: (()=>{try{return patient.extension.filter(obj => obj.url.includes("race")).map(obj => obj.extension[0].valueCoding.display);}catch(e){return undefined;}})(),
        ethnicity: (()=>{try{return patient.extension.filter(obj => obj.url.includes("ethnicity")).map(obj => (()=>{try{return obj.extension[0].valueCoding.display;}catch(e){return obj.extension[0].valueString;}})());}catch(e){return undefined;}})(),
        contact: {
          given: (()=>{try{return patient.contact[0].name.given[0];}catch(e){return undefined;}})(),
          family: (()=>{try{return patient.contact[0].name.family;}catch(e){return undefined;}})(),
          relationship: (()=>{try{return patient.contact[0].relationship;}catch(e){return undefined;}})(),
          phone: {
            value: (()=>{try{return patient.contact[0].telecom[0].value;}catch(e){return undefined;}})(),
            use: (()=>{try{return exports.phoneUseEnums[patient.contact[0].telecom[0].use];}catch(e){return undefined;}})()
          }
        },
        address: {
          use: (()=>{try{return exports.addressUseEnums[patient.address[0].use];}catch(e){return undefined;}})(),
            line: (()=>{try{return patient.address[0].line[0];}catch(e){return undefined;}})(),
            line2: (()=>{try{return patient.address[0].line[1];}catch(e){return undefined;}})(),
            city: (()=>{try{return patient.address[0].city;}catch(e){return undefined;}})(),
            state: (()=>{try{return patient.address[0].state;}catch(e){return undefined;}})(),
            postalCode: (()=>{try{return patient.address[0].postalCode;}catch(e){return undefined;}})(),
            country: (()=>{try{return patient.address[0].country;}catch(e){return undefined;}})(),
        },
        language: (()=>{try{return patient.communication[0].language.text;}catch(e){return undefined;}})(),
        link: (()=>{try{return patient.link[0].other.reference;}catch(e){return undefined;}})(),
    };

    return model;
};
