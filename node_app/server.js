const express = require('express');
const app = express();
const axios = require('axios').default;
const base = 'http://hapi:8080/hapi-fhir-jpaserver/fhir'

app.use(express.json());
app.set('json spaces', 2);

let headers = {
    'content-type': 'application/fhir+json'
}


app.post("/Practitioner", (req, res) => {
  let obj = req.body;
  for (practitioner of obj.Practitioner) {
    let resource = {
      resourceType: "Practitioner",
      name: [
        {
          family: practitioner.family,
          given: [practitioner.given],
          suffix: [practitioner.suffix],
        },
      ],
      telecom: [
        // add later
      ],
      gender: practitioner.gender,
      birthDate: practitioner.birthDate,
      address: [
        {
          line: [practitioner.address.line],
          city: practitioner.address.city,
          state: practitioner.address.state,
          postalCode: practitioner.address.postalCode,
          country: practitioner.address.country,
        },
      ],
      photo: [
        {
          url: "", // add later
          title: `Photo of ${practitioner.given} ${practitioner.family} ${practitioner.suffix}`,
        },
      ],
      qualification: [
                {
                    code: {
                            coding: [
                                {
                                    code: practitioner.qualificationCode
                                }
                            ],
                            text: "",

                    }
                    
                }
        ],
    // post resource
    axios
      .post(`${base}/Practitioner`, resource, headers)
      .then((response) => {
        res.json(response.data);
      })
      .catch((e) => res.send(e));
  }
});



const port = process.env.PORT || 3000
app.listen(port, () => console.log(`Listening on port ${port}`));