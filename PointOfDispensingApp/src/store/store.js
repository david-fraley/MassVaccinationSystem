import Vuex from 'vuex'
import Vue from 'vue'


Vue.use(Vuex)

export default new Vuex.Store({
    
    state: {
        activeWorkflowState: 'NO_PATIENT_LOADED',
        patientResource: {},
        locationResource: {
            id: 'example',
            name: 'Western Lakes FD'
        },
        encounterResource: {},
        appointmentResource: {},
        immunizationResource: {},
        patientHistory: [],
        practitionerResource: {
            id: "example",
            family: "White",
            given: "Betty"
        },
        screeningResponses: {
            vaccinationDecision: '',
            screeningQ1: '',
            screeningQ2: '',
            screeningQ2b: '',
            screeningQ3a: '',
            screeningQ3b: '',
            screeningQ3c: '',
            screeningQ4: '',
            screeningQ5: '',
            screeningQ6: '',
            screeningQ7: '',
            screeningQ8: '',
            screeningComplete: false
        },
        config: {
            vaccine: "COVID",
            route: "Intramuscular injection",
            education: ["education"],
            series: "series",
            seriesDoses: 2,
        }
    },

    getters: {
        activeWorkflowState: state => {
            return state.activeWorkflowState
        },
        isCheckInPageDisabled: state => {
            //Check-In page is not accessible before the patient record has been loaded or after the patient has been discharged
            return (state.activeWorkflowState == 'NO_PATIENT_LOADED')
        },
        isCheckInPageReadOnly: state => {
            //Check-In page is "read only" unless the patient record has just been retrieved
            return (state.activeWorkflowState != 'RECORD_RETRIEVED')
        },
        isPatientHistoryPageDisabled: state => {
            //Patient History page is not accessible before the patient record has been loaded 
            return (state.activeWorkflowState == 'NO_PATIENT_LOADED')
        },
        isVaccinationEventPageDisabled: state => {
            //Vaccination Event page is not accessible before the patient has been checked 
            return ((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'RECORD_RETRIEVED'))
        },
        isVaccinationEventPageReadOnly: state => {
            //Vaccination Event page is "read only" after the patient has been discharged
            return (state.activeWorkflowState == 'DISCHARGED')
        },
        isAdverseReactionPageDisabled: state => {
            //The Adverse Reaction page is only accessible after the vaccine has been administered (at which point, the patient is discharged)
            return (state.activeWorkflowState != 'DISCHARGED')
        },
        isConsentScreeningPageDisabled: state => {
            //Consent and Screening page is not accessible before the patient record has been loaded 
            return (state.activeWorkflowState == 'NO_PATIENT_LOADED')
        },
        isConsentScreeningPageReadOnly: state => {
            //Consent and Screening page is "read only" after the patient has been discharged
            return (state.activeWorkflowState == 'DISCHARGED')
        },
        isDischargePageDisabled: state => {
            //The Discharge page is only accessible after the vaccine has been administered (at which point, the patient is discharged)
            return (state.activeWorkflowState != 'DISCHARGED')
        },
        isConfigurationPageDisabled: state => {
            //The Configuration page is only accessible before a patient has been checked in or after a patient has been discharged
            //(in other words, a user cannot go to the Configuration page while a patient record is actively in use)
            return ((state.activeWorkflowState != 'NO_PATIENT_LOADED') && (state.activeWorkflowState != 'DISCHARGED'))
        },
    },

    mutations: {
        patientRecordRetrieved(state, payload) {            
            console.log('patient record retrieved')
            console.log(state.activeWorkflowState)
            
            //TO DO:  retrieve encounter resource, immunization resource, and appointment resource 'status' fields to determine the workflow state
            if((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'DISCHARGED') || (state.activeWorkflowState == 'RECORD_RETRIEVED'))
            {
                state.activeWorkflowState = 'RECORD_RETRIEVED'

                //reset patient-specific data
                state.encounterResource = {};
                state.appointmentResource = {};
                state.immunizationResource.lotNumber = '',
                state.immunizationResource.expirationDate = '',
                state.immunizationResource.manufacturer = '',
                state.immunizationResource.doseQuantity = '',
                state.immunizationResource.doseNumber = '',
                state.immunizationResource.immunizationStatus = '',
                state.immunizationResource.immunizationTimeStamp = '',
                state.immunizationResource.healthcarePractitioner = 'White, Betty',
                state.immunizationResource.site = '',
                state.immunizationResource.route = 'Injection',
                state.immunizationResource.notes = '',
                state.immunizationResource.notAdministeredReason = ''
                state.screeningResponses.vaccinationDecision = '',
                console.log('Reset screening question responses')
                state.screeningResponses.screeningQ1 = '',
                state.screeningResponses.screeningQ2 = '',
                state.screeningResponses.screeningQ2b = '',
                state.screeningResponses.screeningQ3a = '',
                state.screeningResponses.screeningQ3b = '',
                state.screeningResponses.screeningQ3c = '',
                state.screeningResponses.screeningQ4 = '',
                state.screeningResponses.screeningQ5 = '',
                state.screeningResponses.screeningQ6 = '',
                state.screeningResponses.screeningQ7 = '',
                state.screeningResponses.screeningQ8 = '',
                state.screeningResponses.screeningComplete = false
            }

            state.patientResource = payload.Patient;
            state.patientHistory = payload.Immunization;
            state.encounterResource = payload.Encounter;
            state.appointmentResource = payload.Appointment;
            console.log(payload);

        },
        patientAdmitted(state, payload) {
            state.activeWorkflowState = 'ADMITTED'
            console.log(state.activeWorkflowState)
            state.encounterResource = payload.Encounter
            state.appointmentResource = payload.Appointment
        },
        vaccinationScreeningUpdate (state, screeningResponsesPayload) {
            state.screeningResponses.vaccinationDecision = screeningResponsesPayload.vaccinationDecision
            state.screeningResponses.screeningQ1 = screeningResponsesPayload.screeningQ1
            state.screeningResponses.screeningQ2 = screeningResponsesPayload.screeningQ2
            state.screeningResponses.screeningQ2b = screeningResponsesPayload.screeningQ2b
            state.screeningResponses.screeningQ3a = screeningResponsesPayload.screeningQ3a
            state.screeningResponses.screeningQ3b = screeningResponsesPayload.screeningQ3b
            state.screeningResponses.screeningQ3c = screeningResponsesPayload.screeningQ3c
            state.screeningResponses.screeningQ4 = screeningResponsesPayload.screeningQ4
            state.screeningResponses.screeningQ5 = screeningResponsesPayload.screeningQ5
            state.screeningResponses.screeningQ6 = screeningResponsesPayload.screeningQ6
            state.screeningResponses.screeningQ7 = screeningResponsesPayload.screeningQ7
            state.screeningResponses.screeningQ8 = screeningResponsesPayload.screeningQ8
            state.screeningResponses.screeningComplete = screeningResponsesPayload.screeningComplete
        },
        vaccinationComplete(state, vaccinationCompletePlayload) {
            state.activeWorkflowState = 'VACCINATION_COMPLETE'
            console.log(state.activeWorkflowState)
            state.immunizationResource = vaccinationCompletePlayload
        },
        vaccinationCanceled(state, vaccinationCanceledPlayload) {
            state.activeWorkflowState = 'VACCINATION_CANCELED'
            console.log(state.activeWorkflowState)
            state.immunizationResource = vaccinationCanceledPlayload
        },
        patientDischarged(state, payload) {
            state.activeWorkflowState = 'DISCHARGED'
            console.log(state.activeWorkflowState)
            state.encounterResource = payload.Encounter;
            state.appointmentResource = payload.Appointment;
        },
        unknownErrorCondition(state) {
            state.activeWorkflowState = 'ERROR'
            console.log(state.activeWorkflowState)
        },
        patientHistory(state, payload){
            state.patientHistory = payload;
        }

    },

    actions: {
        patientRecordRetrieved(context, payload) {
            context.commit('patientRecordRetrieved', payload)
        },
        patientAdmitted(context, payload) {
            context.commit('patientAdmitted', payload)
        },
        vaccinationScreeningUpdate(context, screeningResponsesPayload) {
            context.commit('vaccinationScreeningUpdate', screeningResponsesPayload)
        },
        vaccinationComplete(context, vaccinationCompletePlayload) {
            context.commit('vaccinationComplete', vaccinationCompletePlayload)
        },
        vaccinationCanceled(context, vaccinationCanceledPlayload) {
            context.commit('vaccinationCanceled', vaccinationCanceledPlayload)
        },
        patientDischarged(context, payload) {
            context.commit('patientDischarged', payload)
        },
        unknownErrorCondition(context) {
            context.commit('unknownErrorCondition')
        },
        patientHistory(context, payload){
            context.commit('patientHistory', payload)
        }
    },
    activeWorkflowStateEnum:
    {
        NO_PATIENT_LOADED: 0,
        RECORD_RETRIEVED: 1,
        ADMITTED: 2,
        VACCINATION_COMPLETE: 3,
        VACCINATION_CANCELED: 4,
        DISCHARGED: 5,
        ERROR: 6
    }

});