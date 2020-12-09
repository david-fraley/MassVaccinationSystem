/*
Encounter {
  status : enum (planned, arrived, in-progress, finished, cancelled)
  class : enum (FLD)
  subject : string "Patient/id"
  appointment : string "Appointment/id"
  start : string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  end : string "YYYY, YYYY-MM, YYYY-MM-DD or YYYY-MM-DDThh:mm:ss+zz:zz"
  location : string "Location/id"
}
{
  "Encounter": {
    "status" : "planned",
    "class" : "FLD",
    "subject" : "Patient/example",
    "appointment" : "Appointment/707",
    "start" : "2020-09-17T05:10:19+00:00",
    "end" : "2020-09-18T05:15:19+00:00",
    "location" : "Location/id"
  }
}
*/