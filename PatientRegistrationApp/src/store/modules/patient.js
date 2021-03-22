export default {
  state: {
    patient: {
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
      contactDisclaimerRead: false,
      allowContact: false,
    },
    resetPatient: {
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
      addressUse: "",
      addressLine: "",
      addressLine2: "",
      addressCity: "",
      addressState: "",
      addressPostalCode: "",
      addressCountry: "USA",
    },
  },
  getters: {
  },
  mutations: {
    setPatientData(state, { data }) {
      state.patient.family = data.familyName;
      state.patient.given = data.givenName;
      state.patient.middle = data.middleName;
      state.patient.suffix = data.suffix;
      state.patient.birthDate = data.birthDate;
      state.patient.gender = data.gender;
      state.patient.race = data.race;
      state.patient.ethnicity = data.ethnicity;
      state.patient.language = data.preferredLanguage;

      state.patient.address.use = data.addressType;
      // this will to add line 2, but trim off empty spaces if none exists
      state.patient.address.line = (data.lineAddress1 + " " + data.lineAddress2).trim();
      state.patient.address.city = data.cityAddress;
      state.patient.address.state = data.stateAddress;
      state.patient.address.country = data.countryAddress;
      state.patient.address.postalCode = data.postalCode;

      if (data.patientPhoneNumber) {
        state.patient.phone.push({
          value: data.patientPhoneNumber,
          use: data.patientPhoneNumberType,
        });
      }
      if (data.patientEmail) {
        state.patient.email.push(data.patientEmail);
      }

      state.patient.contact.family =
        data.emergencyContactFamilyName;
      state.patient.contact.given = data.emergencyContactGivenName;
      state.patient.contact.phone.value = data.emergencyContactPhoneNumber;
      state.patient.contact.phone.use = data.emergencyContactPhoneNumberType;
      // missing the contact type?

      // what to do with this?
      // state.patient. = data.patientPhoto;
      // state.patient. = data.patientPhotoSrc;
    },
    updatePatient(state, { field, value }) {
      state.patient[field] = value;
    },
    resetPatient(state) {
      state.patient = Object.assign({}, state.resetPatient);
    },
  },
  actions: {
    setPatient({ commit }, { data }) {
      commit('setPatientData', { data });
    },
  }
};
