function newPatient() {
  return Object.assign({}, {
    identifier: "",
    family: "",
    given: "",
    middle: "",
    suffix: "",
    gender: "",
    birthDate: "",
    race: "",
    ethnicity: "",
    language: "English",
    phone: "",
    phoneType: "",
    email: "",
    contactFamily: "",
    contactGiven: "",
    contactPhone: "",
    contactPhoneType: "",
    contactRelationship: "",
    addressUse: "",
    addressLine: "",
    addressLine2: "",
    addressCity: "",
    addressState: "",
    addressPostalCode: "",
    addressCountry: "USA",
    verifyInfo: false,
    allowContact: false,
  });
}

export default {
  state: {
    hasSubmitted: false,
    patient: newPatient(),
  },
  getters: {
  },
  mutations: {
    updatePatient(state, { field, value }) {
      state.patient[field] = value;
    },
    resetPatient(state) {
      state.hasSubmitted = false;
      state.patient = newPatient();
    },
  },
  actions: {
    setPatient({ commit }, { data }) {
      commit('setPatientData', { data });
    },
  }
};
