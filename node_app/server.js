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
    let obj = req.body:
    for (practitioner of obj.Practitioner) {
        let resource = {
            resourceType: "Practitioner",
            name: [
             {
                family: practitioner.family,
                given: [practitioner.given],
                suffix: [practitioner.suffix]
             }
            ],
            telecom: [ //add later
                ],
            gender: practitioner.gender,
            birthdate: practitioner.birthdate,
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
                    url: "", //add later
                    title: 'Photo of ${practitioner.given} ${practitioner.family} ${patient.suffix}',
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

        };
        
    axios
    .post(`${base}/Practitioner`, resource, headers)
    .then((response) => {
      res.json(response.data);
    })
    .catch((e) => res.send(e));

    }
});



// Pass request to HAPI FHIR server
app.all('/Patient*', (req, res) => {

    axios({
        method: req.method,
        url: `${base}${req.url}`,
        data: req.body,
        headers: headers
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
            console.log('Error', error.message);
        }
        console.log(error.config);
    })
    .then(function () {
        // always executed
    });
});



const port = process.env.PORT || 3000
app.listen(port, () => console.log(`Listening on port ${port}`));