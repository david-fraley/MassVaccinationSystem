/**
 * Endpoints for Mirth Connect
 * 
 * @author Rob Rucker
 * @author www.massvaxx.com
 */
const configs = require("../config/server.js");
const axios = require("axios").default;

module.exports = function (req, res) {
    const immId = req.query.immunization;
    if (process.env.DEVELOPMENT == 1) {
        console.log("Immunization ID: " + immId);
    }

    return axios.get(`${configs.fhirUrlBase}/Immunization?_id=${immId}&_include=Immunization:patient`).then((response) => {
        const mirthBundle = response.data;

        if (process.env.DEVELOPMENT == 1) {
            console.log(response);
        }

        axios.post(`${configs.mirthUrl}`, mirthBundle).then((immResponse) => {
            if (process.env.DEVELOPMENT == 1) {
                console.log(immResponse);
            }

            res.json(immResponse.data);
        }).catch((e) => {
            res.status(400).json({
                error: e.response ? e.response.data : e.message,
            });
        });
    }).catch((e) => {
        res.status(400).json({
            error: e.response ? e.response.data : e.message,
        });
    });
};