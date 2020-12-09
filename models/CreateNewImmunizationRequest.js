/*
Immunization {
  vaccine: string
  manufacturer: string "Organization/id"
  lotNumber: string
  expiration: string "YYYY, YYYY-MM, or YYYY-MM-DD"
  patient: string "Patient/id"
  encounter: string "Encounter/id"
  status: enum (completed, entered-in-error, not-done)
  statusReason: enum (IMMUNE, MEDPREC, OSTOCK, PATOBJ) i.e. immunity, medical precaution, product out of stock, patient objection
  occurrence: string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  primarySource: boolean
  location: string "Location/id"
  site: enum (LA, RA) i.e. left arm, right arm
  route: enum (IDINJ, IM, NASINHLC, IVINJ, PO, SQ, TRNSDERM) i.e. , inj intramuscular, inhalation nasal, inj intravenous, swallow, inj subcutaneous, transdermal
  doseQuantity: decimal
  doseUnit: string
  performer: [string] "{Practitioner/PractitionerRole/Organization}/id"
  education: [string] "url"
  series: string,
  doseNumber: int
}
{
  "Immunization": {
    "vaccine": "FLUVAX",
    "manufacturer": "Organization/f001",
    "lotNumber": "AAJN11K",
    "expiration": "2015-02-15",
    "patient": "Patient/example",
    "encounter": "Encounter/715",
    "status": "completed",
    "statusReason": "OSTOCK",
    "occurrence": "2013-01-10",
    "primarySource": true,
    "location": "Location/id",
    "site": "LA",
    "route": "IVINJ",
    "doseQuantity": 5,
    "doseUnit": "mg",
    "performer": ["Practitioner/f201"],
    "education": [""],
    "series": "series",
    "doseNumber": 1
  }
}
*/