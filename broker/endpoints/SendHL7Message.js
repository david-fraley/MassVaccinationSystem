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

    const includes = "_include=Immunization:patient&_include=Immunization:location&_include=Immunization:performer";
	let url = `${configs.fhirUrlBase}/Immunization?_id=${immId}&${includes}`;
	if (process.env.DEVELOPMENT == 1) {
        console.log("SendHL7Message URL: " + url);
    }

    return axios.get(url).then((response) => {
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