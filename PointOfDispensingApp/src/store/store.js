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
        vaccinationComplete(state) {
            state.workflowState = 'VACCINATION_COMPLETE'
        },
        patientDischarged(state) {
            state.workflowState = 'DISCHARGED'
        },

    },

    actions: {
        patientRecordRetrieved(context, patientResourcePayload) {
            context.commit('patientRecordRetrieved', patientResourcePayload)
        },
        patientAdmitted(context, encounterResourcePayload) {
            context.commit('patientAdmitted', encounterResourcePayload)
        },
        vaccinationComplete(context) {
            context.commit('vaccinationComplete')
        },
        patientDischarged(context) {
            context.commit('patientDischarged')
        },

    }

});