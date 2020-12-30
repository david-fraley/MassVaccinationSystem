import Vuex from 'vuex'
import Vue from 'vue'


Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        workflowState: 'INITIAL'
            
    },

    getters: {

    },

    mutations: {
        patientRecordRetrieved(state) {
            state.workflowState = 'RECORD_RETRIEVED'
        },
        patientAdmitted(state) {
            state.workflowState = 'ADMITTED'
        },
        vaccinationComplete(state) {
            state.workflowState = 'VACCINATION_COMPLETE'
        },
        patientDischarged(state) {
            state.workflowState = 'DISCHARGED'
        },

    },

    actions: {
        patientRecordRetrieved(context) {
            context.commit('patientRecordRetrieved')
        },
        patientAdmitted(context) {
            context.commit('patientAdmitted')
        },
        vaccinationComplete(context) {
            context.commit('vaccinationComplete')
        },
        patientDischarged(context) {
            context.commit('patientDischarged')
        },

    }

});