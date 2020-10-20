const Joi = require('joi');
const express = require('express');
const app = express();
const request = require('request');
const axios = require('axios').default;


app.use(express.json());

const courses = [
    { id: 1, name: 'course1'},
    { id: 2, name: 'course2'},
    { id: 3, name: 'course3'},
]

// Get all patients
app.get('/Patients', (req, res) => {
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Patient')
        .then(function (response) {
            // handle success
            console.log(JSON.stringify(response['data']));
            res.send(JSON.stringify(response['data']));
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
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Organization')
        .then(function (response) {
            // handle success
            console.log(JSON.stringify(response['data']));
            res.send(JSON.stringify(response['data']));
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
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Patient?gender=male')
        .then(function (response) {
            // handle success
            console.log(JSON.stringify(response['data']));
            res.send(JSON.stringify(response['data']));
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

app.get('/', (req, res) => {
    res.send('Hello World')
});

app.get('/api/courses', (req, res) => {
    res.send(courses)
});

app.get('/api/courses/:id', (req, res) => {
    const course = courses.find(c => c.id === parseInt(req.params.id))

    if (!course) return res.status(404).send('The course with the given id was not found')
    res.send(course)
});

app.get('/api/courses/:year/:month', (req,res) => {
    res.send(req.query);
});

app.post('/api/courses', (req, res) => {

    const course = {
        id: courses.length + 1,
        name: req.body.name
    };
    
    courses.push(course)
    res.send(course)
});

app.put('/api/courses/:id', (req, res) => {

    // Check if course exists
    const course = courses.find(c => c.id === parseInt(req.params.id))
    if (!course) return res.status(404).send('The course with the given id was not found')

    // Validate for erroes
    const result = validateCourse(req.body);
    if (result.error) {
        return res.status(400).send(result.error.details[0].message)
    }

    // Update Course
    course.name = req.body.name;
    res.send(course);
});

function validateCourse(course) {
    const schema = Joi.object({
        name: Joi.string()
            .min(3)
            .required()
    })

    const result = schema.validate(course);
    return result
}


const port = process.env.PORT || 3000
app.listen(port, () => console.log(`Listening on port ${port}`))