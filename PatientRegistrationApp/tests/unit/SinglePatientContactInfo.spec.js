// Imports Mount method which will create a Mock vue instance (wrapper)
import { shallowMount } from '@vue/test-utils'

// Imports the vue component that we are testing
import SinglePatientContactInfo from '../../src/components/SinglePatientContactInfo'


// Test which checks if the data for our component is a function

// *The describe function in this format is used to create a test suite into components*
describe('SinglePatientContactInfo', () => {

  // The it function is used to run an individual test e.g testing SinglePatient to check if it has data
  it('has data', () => {
    expect(typeof SinglePatientContactInfo.data).toBe('function')
  })

  // Creates a wrapper (a mock vue instance) and checks if it renders
 // it('renders', () => {
  //  const wrapper = shallowMount(SinglePatientContactInfo);
   // expect(wrapper.exists());
 // })


})
















// This creates a shallowMount which is a function that
// creates a Wrapper (a mounted and rendered vue component) to test that everything
// works without actually building and running it


// This is a factory function, basically initiates a vue component without actually setting the values
//const factory = (values = {}) => {
//  return shallowMount(SinglePatientContactInfo, {
//   data () {
//     return {
//       ...values
//     }
//    }
//  })
//}

//describe('SinglePatientContactInfo', () => {
//it('renders a welcome message', () => {
//const wrapper = factory()

// expect(wrapper.find('.phonetype')).toEqual(["Cell", "Landline", "Other"])

// })

//})
