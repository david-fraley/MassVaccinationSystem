const assert = require('chai').assert;
const sayHello = require('../app').sayHello;
const addTest = require('../app').addTest;

describe('App', function() {
    it('sayHello should return hello', function(){
        let result = sayHello();
        assert.equal(result, 'hello');  // compares result to expected string
    });

    it('sayHello should return type string', function() {
        let result = sayHello();
        assert.typeOf(result, 'string');    // checks that the correct data type is returned by the function
    });

    it('addTest should be greater than 0', function() {
        let result = addTest(1,2);
        assert.isAbove(result, 0);  // compares result to expected number
    });

    it('addTest should return type number', function() {
        let result = addTest(1,2);
        assert.typeOf(result, 'number');    // checks that the correct data type is returned by the function
    });
});