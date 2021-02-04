const assert = require('chai').assert;
const app = require('../Appointment');
const data = require('../../testing/examples');


describe('Appointment', function() {
    it('Appointment should return [something]', function(){
      let result = true;

        let obj = JSON.parse(data.Appointment);
        let input = obj.Appointment;
        let fhir = app.toFHIR(input);

        let output = app.toModel(fhir);
        for (let property in input) {
          if (output.property !== input.property) {
            console.log(property);
            console.log(`Expected ${input.property}; received ${output.property}`)
            result = false;
            //break;
          }
        }

        assert(result, 'Test failed');
    });
});