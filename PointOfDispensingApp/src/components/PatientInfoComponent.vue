<template>
  <v-container>
    <v-row>
      <v-col cols="3">
        <v-img
          max-height="220"
          max-width="200"
          src="../assets/blankPicture.png"
        >
        </v-img>
        <div class="font-weight-medium">
          Patient ID: <span class="font-weight-regular">{{ patientId }}</span>
        </div>
      </v-col>
      <v-col cols="9">
        <v-row no-gutters>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.nameRules"
              v-model="patient.family"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.nameRules"
              v-model="patient.given"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              v-model="patient.birthDate"
              required
              :rules="rules.birthdateRules"
              placeholder="MM/DD/YYYY"
              v-mask="'##/##/####'"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Gender</div>
          </v-col>
          <v-col cols="3">
            <v-select
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              :items="genderIdOptions"
              required
              :rules="[(v) => !!v || 'Gender identity field is required']"
              v-model="patient.gender"
            ></v-select>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Age</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientAge"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">
              Preferred Language
            </div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientPreferredLanguage"
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address</div>
          </v-col>
          <v-col cols="9">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientStreetAddress"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-btn v-if="!edit" color="accent" @click="edit = true">
        Edit
      </v-btn>
      <v-btn v-else color="accent" outlined @click="editPatientInfo">
        Save
      </v-btn>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Rules from "@/utils/commonFormValidation"

export default {
  name: "PatientInfoComponent",
  computed: {
    patientId() {
      return this.$store.state.patientResource.id;
    },
    patientAge() {
      const birthdate = this.patient.birthDate;
      if(!birthdate || birthdate.length !== 10)
        return null;

      const [dobMonth, , dobYear] = birthdate.split("/");
      let ageYears = this.currentDate.getFullYear() - dobYear;

      let currentMonth = this.currentDate.getMonth() + 1;
      let ageMonths;
      if(currentMonth >= dobMonth) {
        ageMonths = currentMonth - dobMonth
      }
      else {
        ageYears--;
        ageMonths = 12 + currentMonth - dobMonth;
      }
      
      let ageString = ageYears + " yr(s). " + ageMonths + " mo(s).";
      return ageString;
    },
    patientStreetAddress() {
      let address = this.$store.state.patientResource.address;
      let components = ["line", "city", "state", "postalCode"];
      
      return components.map(component => address[component]).filter(v => v).join(", ");
    },
    patientPreferredLanguage() {
      return this.$store.state.patientResource.language;
    },
  },
  methods: {
    editPatientInfo() {
      this.edit = false;
      // update store
      this.$store.state.patientResource = this.patient;
    }
  },
  data() {
    return {
      rules: Rules,
      edit: false,
      // pass obj by value
      patient: JSON.parse(JSON.stringify(this.$store.state.patientResource)),
      currentDate: new Date(),
      genderIdOptions: ["Male", "Female", "Other", "Decline to answer"],
    };
  },
};
</script>
