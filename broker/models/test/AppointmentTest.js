const assert = require('chai').assert;
const app = require('../Appointment');
const data = require('../../testing/examples');


describe('Appointment', function() {
    it('Appointment should return [something]', function(){
        let object = JSON.parse(data.Appointment);
        console.log(object.participant);
        let result = app.toFHIR(JSON.parse(data.Appointment));
        assert.typeOf(result, 'string');
    });
});