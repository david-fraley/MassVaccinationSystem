/*
Appointment {
  status : enum (proposed, pending, booked, arrived, fulfilled, cancelled, noshow, entered-in-error, checked-in, waitlist)
  participant : [ 
    {
      type : "patient" // note that this type is an extensible
      actor : string
    },
    {
      type : "practitioner" // note that this type is an extensible
      actor : string
    },
    {
      type : "location" // note that this type is an extensible
      actor : string
    },
  ]
  slot : string // note that this is not needed until scheduling is implemented
}
{
  "Appointment":
    {
      "status":"booked",
      "participant":[
        {
          "type":"patient",
          "actor":"Patient/example"
        },
        {
          "type":"pratitioner",
          "actor":"Practitioner/f201"
        },
        {
          "type":"patient",
          "actor":"Patient/102"
        }
      ],
      "slot":"Slot/1253"
    }
}
*/