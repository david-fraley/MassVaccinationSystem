import Vuex from 'vuex'
import Vue from 'vue'


Vue.use(Vuex)

export default new Vuex.Store({
    
    state: {
        activeWorkflowState: 'NO_PATIENT_LOADED',
        patientResource: {},
        locationResource: {
            locationId: '1234567890',
            locationName: 'Western Lakes FD'
        },
        encounterResource: {
            encounterStatus: '',
            encounterTimeStamp: ''
        },
        immunizationResource: {
            lotNumber: '',
            expirationDate: '',
            manufacturer: '',
            doseQuantity: '',
            doseNumber: '',
            immunizationStatus: '',
            immunizationTimeStamp: '',
            healthcarePractitioner: 'White, Betty',
            site: '',
            route: 'Injection',
            notes: '',
            notAdministeredReason: ''
        },
        screeningResponses: {
            vaccinationDecision: '',
            patientInfoConfirmed: '',
            consentFormSigned: '',
            screeningCompleted: '',
            factSheetProvided: '',
            screeningComplete: false
        }
    },

    getters: {
        activeWorkflowState: state => {
            return state.activeWorkflowState
        },
        isCheckInPageDisabled: state => {
            //Check-In page is not accessible before the patient record has been loaded or after the patient has been discharged
            return ((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'DISCHARGED'))
        },
        isPatientHistoryPageDisabled: state => {
            //Patient History page is not accessible before the patient record has been loaded or after the patient has been discharged
            return ((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'DISCHARGED'))
        },
        isVaccinationEventPageDisabled: state => {
            //Vaccination Event page is not accessible before the patient has been checked in or after the patient has been discharged
            return ((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'RECORD_RETRIEVED') || (state.activeWorkflowState == 'DISCHARGED'))
        },
        isAdverseReactionPageDisabled: state => {
            //The Adverse Reaction page is only accessible after the vaccine has been administered and before the patient has been discharged
            return (state.activeWorkflowState != 'VACCINATION_COMPLETE')
        },
        isDischargePageDisabled: state => {
            //The Discharge page is not accessible before the patient has been checked in
            return ((state.activeWorkflowState == 'NO_PATIENT_LOADED') || (state.activeWorkflowState == 'RECORD_RETRIEVED'))
        },
        isConfigurationPageDisabled: state => {
            //The Configuration page is only accessible before a patient has been checked in or after a patient has been discharged
            //(in other words, a user cannot go to the Configuration page while a patient record is actively in use)
            return ((state.activeWorkflowState != 'NO_PATIENT_LOADED') && (state.activeWorkflowState != 'DISCHARGED'))
        },
    },

    mutations: {
        patientRecordRetrieved(state, patientResourcePayload) {
            state.activeWorkflowState = 'RECORD_RETRIEVED'
            state.patientResource = patientResourcePayload;

            //reset patient-specific data
            state.encounterResource.encounterStatus = ''
            state.encounterResource.encounterTimeStamp = ''
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
            state.screeningResponses.patientInfoConfirmed = '',
            state.screeningResponses.consentFormSigned = '',
            state.screeningResponses.screeningCompleted = '',
            state.screeningResponses.factSheetProvided = '',
            state.screeningResponses.screeningComplete = false
        },
        patientAdmitted(state, encounterResourcePayload) {
            state.activeWorkflowState = 'ADMITTED'
            state.encounterResource.encounterStatus = encounterResourcePayload.encounterStatus
            state.encounterResource.encounterTimeStamp = encounterResourcePayload.encounterTimeStamp
        },
        vaccinationScreeningUpdate (state, screeningResponsesPayload) {
            state.screeningResponses.vaccinationDecision = screeningResponsesPayload.vaccinationDecision
            state.screeningResponses.patientInfoConfirmed = screeningResponsesPayload.patientInfoConfirmed
            state.screeningResponses.consentFormSigned = screeningResponsesPayload.consentFormSigned
            state.screeningResponses.screeningCompleted = screeningResponsesPayload.screeningCompleted
            state.screeningResponses.factSheetProvided = screeningResponsesPayload.factSheetProvided
            state.screeningResponses.screeningComplete = screeningResponsesPayload.screeningComplete
        },
        vaccinationComplete(state, vaccinationCompletePlayload) {
            state.activeWorkflowState = 'VACCINATION_COMPLETE'
            state.immunizationResource.lotNumber= vaccinationCompletePlayload.lotNumber
            state.immunizationResource.expirationDate= vaccinationCompletePlayload.expirationDate
            state.immunizationResource.manufacturer= vaccinationCompletePlayload.manufacturer
            state.immunizationResource.doseQuantity= vaccinationCompletePlayload.doseQuantity
            state.immunizationResource.doseNumber= vaccinationCompletePlayload.doseNumber
            state.immunizationResource.site= vaccinationCompletePlayload.site
            state.immunizationResource.route= vaccinationCompletePlayload.route
            state.immunizationResource.immunizationStatus= vaccinationCompletePlayload.immunizationStatus
            state.immunizationResource.immunizationTimeStamp= vaccinationCompletePlayload.immunizationTimeStamp
            state.immunizationResource.healthcarePractitioner= vaccinationCompletePlayload.healthcarePractitioner
            state.immunizationResource.notes= vaccinationCompletePlayload.notes
        },
        vaccinationCanceled(state, vaccinationCanceledPlayload) {
            state.activeWorkflowState = 'VACCINATION_CANCELED'
            state.immunizationResource.immunizationStatus= vaccinationCanceledPlayload.immunizationStatus
            state.immunizationResource.immunizationTimeStamp= vaccinationCanceledPlayload.immunizationTimeStamp
            state.immunizationResource.healthcarePractitioner= vaccinationCanceledPlayload.healthcarePractitioner
            state.immunizationResource.notAdministeredReason= vaccinationCanceledPlayload.notAdministeredReason
            state.immunizationResource.notes= vaccinationCanceledPlayload.notes
        },
        patientDischarged(state, encounterResourcePayload) {
            state.activeWorkflowState = 'DISCHARGED'
            state.encounterResource.encounterStatus = encounterResourcePayload.encounterStatus
            state.encounterResource.encounterTimeStamp = encounterResourcePayload.encounterTimeStamp
        },
        unknownErrorCondition(state) {
            state.activeWorkflowState = 'ERROR'
        }

    },

    actions: {
        patientRecordRetrieved(context, patientResourcePayload) {
            context.commit('patientRecordRetrieved', patientResourcePayload)
        },
        patientAdmitted(context, encounterResourcePayload) {
            context.commit('patientAdmitted', encounterResourcePayload)
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
        patientDischarged(context, encounterResourcePayload) {
            context.commit('patientDischarged', encounterResourcePayload)
        },
        unknownErrorCondition(context) {
            context.commit('unknownErrorCondition')
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