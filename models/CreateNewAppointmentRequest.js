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
  "Appointment": [
    {
      "status" : "booked"
      "participant" : [ 
        {
          "type" : "patient"
          "actor" : "1194419"
        },
        {
          "type" : "pratitioner"
          "actor" : "clinFhirQANGUptCIsgYQnfv6WHol1LOFkB2"
        },
        {
          "type" : "patient"
          "actor" : "628441"
        }
      ]
      "slot" : "1480904"
   ]
}
*/
