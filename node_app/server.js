const express = require('express');
const app = express();
const axios = require('axios').default;


app.use(express.json());

// Get all patients
app.get('/Patients', (req, res) => {
    
    axios.get('http://hapi:8080/hapi-fhir-jpaserver/fhir/Patient')
        .then((response) => {
            // handle success
            res.send(JSON.stringify(response.data));
        })
        .catch(function (error) {
            // handle error
            console.log(error);
            res.send(error);
        })
        .then(function () {
            // always executed
        });
});

// Get all organizations
app.get('/Organizations', (req, res) => {
    
    axios.get('http://hapi:8080/hapi-fhir-jpaserver/fhir/Organization')
        .then((response) => {
            // handle success
            res.send(JSON.stringify(response.data));
        })
        .catch(function (error) {
            // handle error
            console.log(error);
            res.send(error);
        })
        .then(function () {
            // always executed
        });
});

// Search patients
app.get('/Patients2', (req, res) => {
    
    axios.get('http://hapi:8080/hapi-fhir-jpaserver/fhir/Patient?gender=male')
        .then((response) => {
            // handle success
            res.send(JSON.stringify(response.data));
        })
        .catch(function (error) {
            // handle error
            console.log(error);
            res.send(error);
        })
        .then(function () {
            // always executed
        });
});

const port = process.env.PORT || 3000
app.listen(port, () => console.log(`Listening on port ${port}`));