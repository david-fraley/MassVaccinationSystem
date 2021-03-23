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
            <v-select
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              :items="languageOptions"
              required
              :rules="[(v) => !!v || 'Preferred language field is required']"
              v-model="patient.language"
            ></v-select>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address Line 1</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="[(v) => !!v || 'Address Line field is required']"
              v-model="patient.address.line"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address Line 2</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              v-model="patient.address.line2"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">City</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="[(v) => !!v || 'City field is required']"
              v-model="patient.address.city"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="1">
            <div class="font-weight-medium secondary--text">State</div>
          </v-col>
          <v-col cols="1">
            <v-select
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="[v => !!v || 'State field is required']"
              v-model="patient.address.state"
              :items="stateOptions">
            </v-select>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="1">
            <div class="font-weight-medium secondary--text">Zip Code</div>
          </v-col>
          <v-col cols="1">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.postalCodeRules"
              v-model="patient.address.postalCode"
              v-mask="postalCodeMask"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Emergency Contact Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.nameRules"
              v-model="patient.contact.family"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Emergency Contact First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.nameRules"
              v-model="patient.contact.given"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Emergency Contact Phone Number</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              required
              :rules="rules.phoneNumberRules"
              v-mask="'(###)###-####'"
              v-model="patient.contact.phone.value">
            </v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Phone Number</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              :rules="phoneNumberRules"
              v-mask="'(###)###-####'"
              v-model="patient.phone[0].value">
            </v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Email</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              :filled="!edit"
              dense
              :readonly="!edit"
              outlined
              :rules="[(v) => /^[\s]*$|.+@.+\..+/.test(v) || 'Please provide a valid e-mail address']"
              v-model="patient.email[0]">
            </v-text-field>
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
    postalCodeMask() {
      let mask = '#####';
      if (this.patient.address.postalCode.length >= 6) {
        mask = '#####-####';
      }
      return mask;
    }
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
      languageOptions: ["English", "Spanish"],
      stateOptions:[
				'AL', 'AK', 'AS', 'AZ',
				'AR', 'CA', 'CO', 'CT',
				'DE', 'DC', 'FM',
				'FL', 'GA', 'GU', 'HI', 'ID',
				'IL', 'IN', 'IA', 'KS', 'KY',
				'LA', 'ME', 'MH', 'MD',
				'MA', 'MI', 'MN', 'MS',
				'MO', 'MT', 'NE', 'NV',
				'NH', 'NJ', 'NM', 'NY',
				'NC', 'ND', 'MP', 'OH',
				'OK', 'OR', 'PW', 'PA', 'PR',
				'RI', 'SC', 'SD', 'TN',
				'TX', 'UT', 'VT', 'VI', 'VA',
				'WA', 'WV', 'WI', 'WY',
      ],
      phoneNumberRules: [
        (v) => !v || v.length === 13 || "Phone number must be 10 digits"
      ]
    };
  },
};
</script>
