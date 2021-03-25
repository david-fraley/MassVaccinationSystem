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
          Patient ID: <span class="font-weight-regular">{{ patient.id }}</span>
        </div>
      </v-col>
      <v-col cols="9">
        <v-row no-gutters>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              required
              :rules="rules.nameRules"
              :value="patient.given"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              required
              :rules="rules.nameRules"
              :value="patient.family"
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patient.birthDate"
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
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Gender</div>
          </v-col>
          <v-col cols="3">
            <v-select
              filled
              dense
              readonly
              outlined
              :items="genderIdOptions"
              required
              :rules="[(v) => !!v || 'Gender identity field is required']"
              :value="patient.gender"
            ></v-select>
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
            <v-select
              filled
              dense
              readonly
              outlined
              :items="languageOptions"
              required
              :rules="[(v) => !!v || 'Preferred language field is required']"
              :value="patient.language"
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
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
    patient() {
      return this.$store.state.patientResource;
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
  },
  data() {
    return {
      rules: Rules,
      currentDate: new Date(),
      genderIdOptions: ["Male", "Female", "Other", "Decline to answer"],
      languageOptions: ["English", "Spanish"]
    };
  },
};
</script>
