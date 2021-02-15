const assert = require('chai').assert;
const app = require('../Appointment');
const data = require('../../testing/examples');


describe('Appointment', function() {
    it('toFHIR should be equal to toModel', function(){
      let result = true;

        let obj = JSON.parse(data.Appointment);
        let input = obj.Appointment;
        let fhir = app.toFHIR(input);

        let output = app.toModel(fhir);
        
        for (let property in input) {
            //console.log(input.property);
          if (output.property !== input.property) {
            console.log(property);
            
            //console.log(`Expected ${input.property}; received ${output.property}`)
            result = false;
            assert.fail(`Expected ${input.property}; received ${output.property}`);
            //break;
          }
        }

        /*
        for (let property in output) {
            console.log(output.property);
        }
        */

        assert(result, 'Test failed');
    });
});