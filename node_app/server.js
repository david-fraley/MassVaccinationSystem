const express = require('express');
const app = express();
const axios = require('axios').default;
const base = 'http://hapi:8080/hapi-fhir-jpaserver/fhir'

app.use(express.json());
app.set('json spaces', 2);

let headers = {
    'content-type': 'application/fhir+json'
}

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