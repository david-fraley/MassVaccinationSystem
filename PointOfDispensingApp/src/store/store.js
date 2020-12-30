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
        vaccinationComplete(state) {
            state.workflowState = 'VACCINATION_COMPLETE'
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
        vaccinationComplete(context) {
            context.commit('vaccinationComplete')
        },
        patientDischarged(context, encounterResourcePayload) {
            context.commit('patientDischarged', encounterResourcePayload)
        },

    }

});