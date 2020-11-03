//file name is 'config.js'

export default {
    registrationPages:
    {
        GREETING_PAGE: 1,
        SINGLE_PATIENT_HOME_ADDRESS_PAGE: 2,
        SINGLE_PATIENT_CONTACT_INFO_PAGE: 3,
        SINGLE_PATIENT_PERSONAL_INFO_PAGE: 4,
        SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE: 5,
        SINGLE_PATIENT_REVIEW_SUBMIT_PAGE: 6,
        HOUSEHOLD_REGISTER_NUMBER_PAGE: 2,
        HOUSEHOLD_HOME_ADDRESS_PAGE: 3,
        HOUSEHOLD_CONTACT_INFO_PAGE: 4,
        HOUSEHOLD_PERSONAL_INFO_1_PAGE: 5,
        HOUSEHOLD_EMERGENCY_CONTACT_PAGE: 6,
        HOUSEHOLD_PERSONAL_INFO_N_PAGE: 7,
        HOUSEHOLD_REVIEW_SUBMIT_PAGE: 8   
    },

    selectedRegistrationPath:
    {
        NO_PATH_SELECTED: 0,
        SINGLE_PATIENT_REGISTRATION_PATH: 1,
        HOUSEHOLD_REGISTRATION_PATH: 2
    }
}
export const FontSizes = Object.freeze({
    FONTLARGE: '3em', 
    FONTSMALL: '1em', 
    FONTMEDIUM: '1.5em'
    });