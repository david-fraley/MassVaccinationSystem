import Vuex from "vuex";
import Vue from "vue";

Vue.use(Vuex);
// import registration from '@/store/modules/registartion';

export default new Vuex.Store({
  state: {
    patients: [],
    newPatient: {
      family: "",
      given: "",
      middle: "",
      suffix: "",
      gender: "",
      birthDate: "",
      race: "",
      ethnicity: "",
      language: "English",
      phone: [],
      email: [],
      contact: {
        family: "",
        given: "",
        phone: {
          value: "",
          use: "",
        },
      },
      address: {
        use: "",
        line: "",
        city: "",
        state: "",
        postalCode: "",
        country: "",
      },
    },
  },
  getters: {},
  mutations: {
    setPreferredLanguage(state, { index, lang }) {
      state.patients[index].language = lang;
    },
    setHomeAddressData(state, { index, data }) {
      state.patients[index].address.use = data.addressType;
      // this will attempt to add line 2, but trim off empty spaces if none exists
      state.patients[index].address.line = trim(
        data.lineAddress1 + " " + data.lineAddress2
      );
      state.patients[index].address.city = data.cityAddress;
      state.patients[index].address.state = data.stateAddress;
      state.patients[index].address.country = data.countryAddress;
      state.patients[index].address.postalCode = data.postalCode;
    },
    setContactInfo(state, { index, data }) {
      if (data.patientNumber) {
        state.patients[index].email.push({
          value: data.patientNumber,
          use: data.patientNumberType,
        });
      }
      if (data.patientEmail) {
        state.patients[index].phone.push(data.patientEmail);
      }
    },
    setPersonalInfo(state, { index, data }) {
      state.patients[index].family = data.familyName;
      state.patients[index].given = data.givenName;
      state.patients[index].middle = data.middleName;
      state.patients[index].suffix = data.suffix;
      state.patients[index].birthDate = data.birthDate;
      state.patients[index].gender = data.gender;
      state.patients[index].race = data.race;
      state.patients[index].ethnicity = data.ethnicity;
      state.patients[index].language = data.preferredLanguage;

      // state.patients[index]. = data.patientPhoto;
      // state.patients[index]. = data.patientPhotoSrc;
    },
    setEmergencyContactInfo(state, { index, data }) {
      state.patients[index].contact.familyName =
        data.emergencyContactFamilyName;
      state.patients[index].contact.given = data.emergencyContactGivenName;
      state.patients[index].contact.phone.value = emergencyContactPhoneNumber;
      state.patients[index].contact.phone.use = emergencyContactPhoneNumberType;
      // missing the contact type?
    },
    resetPatients(state) {
      state.patients = [];
    },
    addPatient(state, patient) {
      state.patients.push(patient);
    },
  },
  actions: {},
});
