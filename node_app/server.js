const express = require("express");
const app = express();
const axios = require("axios").default;
const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";

app.use(express.json());
app.set("json spaces", 2);

let headers = {
  "content-type": "application/fhir+json",
};

app.post("/Patient", (req, res) => {
  let obj = req.body;
  for (patient of obj.Patient) {
    let resource = {
      resourceType: "Patient",
      name: [
        {
          family: patient.family,
          given: [patient.given],
          suffix: [patient.suffix],
        },
      ],
      telecom: [
        // add later
      ],
      gender: patient.gender,
      birthDate: patient.birthDate,
      address: [
        {
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
          title: `Photo of ${patient.given} ${patient.family} ${patient.suffix}`,
        },
      ],
      contact: [
        {
          relationship: [
            {
              coding: [
                {
                  // unsure
                  system: "http://terminology.hl7.org/CodeSystem/v2-0131",
                  code: patient.contact.relationship,
                  display: patient.contact.relationship,
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
    };
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

    // post resource
    axios
      .post(`${base}/Patient`, resource, headers)
      .then((response) => {
        res.json(response.data);
      })
      .catch((e) => res.send(e));
  }
});

// Pass request to HAPI FHIR server
app.get("/Patient*", (req, res) => {
  axios({
    method: req.method,
    url: `${base}${req.url}`,
    data: req.body,
    headers: headers,
  })
    .then((response) => {
      // handle success
      res.json(response.data);
    })
    .catch(function (error) {
      if (error.response) {
        // The request was made and the server responded with a status code
        let errorCode = error.response.status;
        res.status(errorCode).send(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log(error.request);
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Error", error.message);
      }
      console.log(error.config);
    })
    .then(function () {
      // always executed
    });
});

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}`));
