/*
Observation {
  status : enum (final, amended, corrected)
  category : enum (vital-signs, procedure, exam)
  code : enum (?)
  subject : string
  encounter : string
  effectiveStart: string
  effectiveEnd: string
  performer : [string]
  partOf : string
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