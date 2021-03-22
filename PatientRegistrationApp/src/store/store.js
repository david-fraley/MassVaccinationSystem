import Vuex from "vuex";
import Vue from "vue";

Vue.use(Vuex);
import PatientModuel from '@/store/modules/patient';
import ScreeningQuestions from '@/store/modules/screeningQuestions';

export default new Vuex.Store({
  modules: {
    patient: {
      namespaced: true,
      ...PatientModuel
    },
    screeningQuestions: {
      namespaced: true,
      ...ScreeningQuestions
    }
  },
});
