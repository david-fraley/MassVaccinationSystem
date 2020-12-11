const express = require("express");
const axios = require("axios").default;

const app = express();
app.use(express.json());
app.set("json spaces", 2);

const base = "http://hapi:8080/hapi-fhir-jpaserver/fhir";
const generalEndpoints = ["/Patient*", "/Organization*"];
const headers = {
  "content-type": "application/fhir+json",
};

// Pass GET requests to HAPI FHIR server
app.get(generalEndpoints, (req, res) => {
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



