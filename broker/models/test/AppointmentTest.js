const assert = require('chai').assert;
const app = require('../Appointment');
const data = require('../../testing/examples');


describe('Appointment', function() {
    it('Appointment should return [something]', function(){
        let object = JSON.parse(data.Appointment);
        let result = app.toFHIR(JSON.parse(object.Appointment));
        assert.typeOf(result, 'string');
    });
});