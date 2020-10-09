README(Unit testing)

How to run unit tests

1. Navigate to PatientRegistrationApp directory

   a. Open command prompt
   b. Enter "yarn unit:test"

All test suites should run.


------------------------------------------------------------------


How to write unit tests

1. Navigate to PatientRegistrationApp/tests/unit

2. Naming conventions for testing are as followed:

	JSFILEBEINGTESTED.spec.js

3. Import file being tested (In this example the file will be GreetingPage.vue) as follows:

	import GreetingPage from './../../src/components/GreetingPage'

4. Use the describe('GreetingPage.vue',() =>{ *tests in here*} function to encapsulate all unit tests


5. Inside the describe, each unit test should be created using the it function:

	it('should render blah'),() => {


	}

6. You can run the test using the steps above.
