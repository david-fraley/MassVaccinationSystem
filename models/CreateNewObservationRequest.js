/*
Observation {
  status : enum (final, amended, corrected)
  category : enum (vital-signs, procedure, exam)
  code : enum (?)
  subject : string "Patient/id"
  encounter : string "Encounter/id"
  effectiveStart: string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  effectiveEnd: string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  performer : [string] "{Practitioner/PractitionerRole/Organization}/id"
  partOf : string "Immunization/id"
}
{
  "Observation": {
    "status": "final",
    "category": "procedure",
    "code": "45708-5",
    "subject": "Patient/example",
    "encounter": "Encounter/715",
    "effectiveStart": "2016-05-18T22:33:22Z",
    "effectiveEnd": "2016-05-18T22:38:22Z",
    "performer": [
      "Patient/example"
    ],
    "partOf": "Immunization/1455"
  }
}
*/