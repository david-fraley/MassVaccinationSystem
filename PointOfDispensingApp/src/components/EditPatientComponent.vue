<template>
  <v-container>
    <v-form ref="form" v-model="valid">
      <v-row>
        <v-col cols="12">
          <h2 class="font-weight-medium primary--text">Personal Info</h2>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="3">
          <div class="font-weight-medium secondary--text">First Name</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.given"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="3">
          <div class="font-weight-medium secondary--text">Last Name</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.family"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="3">
          <div class="font-weight-medium secondary--text">Gender</div>
          <v-select
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            :items="genderIdOptions"
            required
            :rules="rules.required"
            v-model="patient.gender"
          ></v-select>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="3">
          <div class="font-weight-medium secondary--text">Preferred Language</div>
          <v-select
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            :items="languageOptions"
            required
            :rules="rules.required"
            v-model="patient.language"
          ></v-select>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Date of Birth</div>
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
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Age</div>
          <v-text-field
            filled
            dense
            readonly
            outlined
            :value="patientAge"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12"><h2 class="font-weight-medium primary--text">Address</h2></v-col>
        <v-col cols="12" sm="12" md="12" lg="6">
          <div class="font-weight-medium secondary--text">Address Line 1</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.address.line"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="12" md="12" lg="5">
          <div class="font-weight-medium secondary--text">Address Line 2</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            v-model="patient.address.line2"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">City</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.address.city"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="2">
          <div class="font-weight-medium secondary--text">State</div>
          <v-select
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.address.state"
            :items="stateOptions">
          </v-select>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="3">
          <div class="font-weight-medium secondary--text">Zip Code</div>
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
      </v-row>
          <v-row>
        <v-col cols="12"><h2 class="font-weight-medium primary--text">Personal Contact</h2></v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Phone Number</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.phoneNumberRules"
            v-mask="'(###)###-####'"
            v-model="phoneNumber">
          </v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Email</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.emailRules"
            v-model="patient.email">
          </v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12"><h2 class="font-weight-medium primary--text">Emergency Contact</h2></v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">First Name</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.contact.given"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Last Name</div>
          <v-text-field
            :filled="!edit"
            dense
            :readonly="!edit"
            outlined
            required
            :rules="rules.required"
            v-model="patient.contact.family"
          ></v-text-field>
        </v-col>
        <v-col cols="12" sm="6" md="6" lg="4">
          <div class="font-weight-medium secondary--text">Phone Number</div>
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
      </v-row>
    </v-form>
    <v-row>
      <v-col cols="12">
        <v-btn v-if="!edit" color="accent" outlined @click="edit = true" :disabled="disabled">
          Edit
        </v-btn>
        <template v-else>
          <v-btn color="accent" @click="editPatient" :disabled="!valid || loading" :loading="loading">
            Save
          </v-btn>
          <v-btn color="accent" outlined @click="resetPatient" class="ml-2">
            Reset
          </v-btn>
        </template>
      </v-col>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Rules from "@/utils/commonFormValidation"
import brokerRequests from "@/brokerRequests";

export default {
  name: "EditPatientComponent",
  computed: {
    disabled() {
      return !!this.$store.state.encounterResource.status;
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
      if (this.patient.address.postalCode && this.patient.address.postalCode.length >= 6) {
        mask = '#####-####';
      }
      return mask;
    }
  },
  methods: {
    editPatient() {
      this.$refs.form.validate();
      if (!this.valid) return;

      this.loading = true;

      if (this.phoneNumber) {
        if (this.patient.phone) this.patient.phone.value = this.phoneNumber;
        else this.patient.phone = {value: this.phoneNumber};
      }

      brokerRequests.updatePatient(this.patient.id, this.patient).then((response) => {
        if (response.data) {
          this.$store.state.patientResource = response.data;
        } else if (response.error) {
          console.log(response.error);
        }
        this.loading = false;
        this.edit = false;
      });
    },
    resetPatient() {
      this.patient = JSON.parse(JSON.stringify(this.$store.state.patientResource));
    }
  },
  data() {
    return {
      rules: Rules,
      edit: false,
      valid: false,
      loading: false,
      // pass obj by value
      patient: JSON.parse(JSON.stringify(this.$store.state.patientResource)),
      phoneNumber: this.$store.state.patientResource.phone ? this.$store.state.patientResource.phone.value : "",
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
    };
  },
};
</script>
