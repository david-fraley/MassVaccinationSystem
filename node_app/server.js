const Joi = require('joi');
const express = require('express');
const app = express();
const axios = require('axios').default;


app.use(express.json());

const courses = [
    { id: 1, name: 'course1'},
    { id: 2, name: 'course2'},
    { id: 3, name: 'course3'},
];

// Get all patients
app.get('/Patients', (req, res) => {
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Patient')
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
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Organization')
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
    
    axios.get('http://localhost:8080/hapi-fhir-jpaserver/fhir/Patient?gender=male')
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

app.get('/', (req, res) => {
    res.send('Hello World');
});

app.get('/api/courses', (req, res) => {
    res.send(courses);
});

app.get('/api/courses/:id', (req, res) => {
    const course = courses.find(c => c.id === parseInt(req.params.id));

    if (!course) return res.status(404).send('The course with the given id was not found');
    res.send(course);
});

app.get('/api/courses/:year/:month', (req,res) => {
    res.send(req.query);
});

app.post('/api/courses', (req, res) => {
    // Validate for errors
    const { error } = validateCourse(req.body);
    if (error) return res.status(400).send(error.details[0].message);

    // Format course to create
    const course = {
        id: courses.length + 1,
        name: req.body.name
    };
    
    // Add course to backend
    courses.push(course);
    res.send(course);
});

app.put('/api/courses/:id', (req, res) => {

    // Check if course exists
    const course = courses.find(c => c.id === parseInt(req.params.id));
    if (!course) return res.status(404).send('The course with the given id was not found');

    // Validate for errors
    const { error } = validateCourse(req.body);
    if (error) return res.status(400).send(error.details[0].message);

    // Update Course
    course.name = req.body.name;
    res.send(course);
});

app.delete('/api/courses/:id', (req, res) => {
    // Check if course exists
    const course = courses.find(c => c.id === parseInt(req.params.id));
    if (!course) return res.status(404).send('The course with the given id was not found');

    // Delete
    const index = courses.indexOf(course);
    courses.splice(index, 1);

    // Return the same course
    res.send(course);
});

function validateCourse(course) {
    const schema = Joi.object({
        name: Joi.string()
            .min(3)
            .required()
    });

    return schema.validate(course);
}


const port = process.env.PORT || 3001
app.listen(port, () => console.log(`Listening on port ${port}`));