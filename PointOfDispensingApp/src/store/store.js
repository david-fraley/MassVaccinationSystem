import Vuex from 'vuex'
import Vue from 'vue'


Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        workflowState: 'INITIAL',
        patientResource: {
            patientId: '',
            patientLastName: '',
            patientFirstName: '',
            patientDateOfBirth: '',
            patientGender: '',
            patientStreetAddress: '',
            patientPreferredLanguage: ''
        },
        locationResource: {
            locationId: '1234567890',
            locationName: 'Western Lakes FD'
        },
        encounterResource: {
            encounterStatus: '',
            encounterTimeStamp: ''
        },
        immunizationResource: {
            lotNumber: 'CR123',
            expirationDate: '12/12/9999',
            manufacturer: 'Pfizer',
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

    },

    mutations: {
        patientRecordRetrieved(state, patientResourcePayload) {
            state.workflowState = 'RECORD_RETRIEVED'
            state.patientResource.patientId = patientResourcePayload.patientId
            state.patientResource.patientLastName = patientResourcePayload.patientLastName
            state.patientResource.patientFirstName = patientResourcePayload.patientFirstName
            state.patientResource.patientDateOfBirth = patientResourcePayload.patientDateOfBirth
            state.patientResource.patientGender = patientResourcePayload.patientGender
            state.patientResource.patientStreetAddress = patientResourcePayload.patientStreetAddress
            state.patientResource.patientPreferredLanguage = patientResourcePayload.patientPreferredLanguage

            //reset patient-specific data
            state.workflowState = 'INITIAL'
            state.encounterResource.encounterStatus = ''
            state.encounterResource.encounterTimeStamp = ''
        },
        patientAdmitted(state, encounterResourcePayload) {
            state.workflowState = 'ADMITTED'
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
            state.workflowState = 'VACCINATION_COMPLETE'
            state.immunizationResource.lotNumber= vaccinationCompletePlayload.lotNumber,
            state.immunizationResource.expirationDate= vaccinationCompletePlayload.expirationDate,
            state.immunizationResource.manufacturer= vaccinationCompletePlayload.manufacturer,
            state.immunizationResource.doseQuantity= vaccinationCompletePlayload.doseQuantity,
            state.immunizationResource.doseNumber= vaccinationCompletePlayload.doseNumber,
            state.immunizationResource.site= vaccinationCompletePlayload.site,
            state.immunizationResource.route= vaccinationCompletePlayload.route,
            state.immunizationResource.immunizationStatus= vaccinationCompletePlayload.immunizationStatus,
            state.immunizationResource.immunizationTimeStamp= vaccinationCompletePlayload.immunizationTimeStamp,
            state.immunizationResource.healthcarePractitioner= vaccinationCompletePlayload.healthcarePractitioner,
            state.immunizationResource.notes= vaccinationCompletePlayload.notes
        },
        vaccinationCanceled(state, vaccinationCanceledPlayload) {
            state.workflowState = 'VACCINATION_CANCELED'
            state.immunizationResource.immunizationStatus= vaccinationCanceledPlayload.immunizationStatus
            state.immunizationResource.immunizationTimeStamp= vaccinationCanceledPlayload.immunizationTimeStamp
            state.immunizationResource.healthcarePractitioner= vaccinationCanceledPlayload.healthcarePractitioner
            state.immunizationResource.notAdministeredReason= vaccinationCanceledPlayload.notAdministeredReason
            state.immunizationResource.notes= vaccinationCanceledPlayload.notes
        },
        patientDischarged(state, encounterResourcePayload) {
            state.workflowState = 'DISCHARGED'
            state.encounterResource.encounterStatus = encounterResourcePayload.encounterStatus
            state.encounterResource.encounterTimeStamp = encounterResourcePayload.encounterTimeStamp
        },

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

    }

});