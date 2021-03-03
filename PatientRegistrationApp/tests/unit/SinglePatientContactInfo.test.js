// Imports Mount method which will create a Mock vue instance (wrapper)
//import { shallowMount } from '@vue/test-utils'

// Imports the vue component that we are testing
import SinglePatientContactInfo from "../../src/components/SinglePatientContactInfo";

// Test which checks if the data for our component is a function

// *The describe function in this format is used to create a test suite into components*
describe("SinglePatientContactInfo", () => {
  // The it function is used to run an individual test e.g testing SinglePatient to check if it has data
  it("has data", () => {
    expect(typeof SinglePatientContactInfo.data).toBe("function");
  });
});
