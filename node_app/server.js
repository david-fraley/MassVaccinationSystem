const express = require("express");
const axios = require("axios").default;

const app = express();
app.use(express.json());
app.set("json spaces", 2);

const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const generalEndpoints = ["/Patient*"];
const headers = {
  "content-type": "application/fhir+json",
};

// Pass request to HAPI FHIR server
app.all(generalEndpoints, (req, res) => {
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
    .catch((error) => handleError(res, error));
});

// Check-in
app.get("/check-in", (req, res) => {
  // ensure subject id (patient/group resource) is provided
  if (req.query.hasOwnProperty("subject")) {
    let subject = req.query.subject;

    axios
      // subjects may have multiple encounters, so limit search to planned encounters
      .get(`${base}/Encounter?subject=${subject}&status=planned`)
      .then((response) => {
        let bundle = response.data;
        try {
          // update encounter status to in-progress
          let encounter = bundle.entry[0].resource;
          encounter.status = "in-progress";

          // update the database with new encounter
          axios
            .put(`${base}/Encounter/${encounter.id}`, encounter, headers)
            .catch((error) => handleError(res, error));
          // update appointment for each member in group
        } catch (e) {
          // handle error
          console.log(bundle);
        }
      })
      .catch((error) => handleError(res, error))
      .then(function () {
        // always executed
      });
  } else {
    // inform user subject id is required
  }
  res.send("");
});

function handleError(res, error) {
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
}

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on port ${port}`));
