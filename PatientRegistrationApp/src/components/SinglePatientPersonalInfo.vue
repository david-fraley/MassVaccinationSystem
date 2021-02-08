<template>
<v-form ref="form" v-model="valid">
  <v-container fluid> 
    <v-row align="center" justify="start">
      <!-- Last name -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-text-field  
          required
          :rules="[v => !!v || 'Last name field is required']"
          v-model="familyName"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Last Name
          </template>
        </v-text-field>
      </v-col>
      <!-- First name -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-text-field 
          required
          :rules="[v => !!v || 'First name field is required']"
          v-model="givenName"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>First Name
          </template>
        </v-text-field>
      </v-col>
      <!-- Middle name -->
      <v-col cols="12" sm="6" md="6" lg="3">
        <v-text-field 
          v-model="middleName"
          label="Middle Name"
          prepend-icon="mdi-blank">
        </v-text-field>
      </v-col>
      <!-- Suffix -->
      <v-col cols="12" sm="6" md="6" lg="1">
        <v-select 
          label="Suffix" 
          :items="suffixOptions" 
          v-model="suffix"
          prepend-icon="mdi-blank">
        </v-select>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Date of Birth -->
        <v-text-field
          v-model="dob"
          :rules="birthdateRules"
          placeholder="MM/DD/YYYY"
          v-mask="'##/##/####'"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Date of Birth
          </template>
        </v-text-field>
      </v-col>
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Gender identity -->
        <v-select
          :items="genderIdOptions"
          required
          :rules="[v => !!v || 'Gender identity field is required']"
          v-model="gender"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Gender Identity
          </template>
        </v-select>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Race -->
        <v-select
          v-model="race"
          :items="raceOptions"
          required
          :rules="[v => !!v || 'Race is required']"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Race
          </template>
        ></v-select>
      </v-col>
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Ethnicity -->
        <v-select
          v-model="ethnicity"
          :items="ethnicityOptions"
          required
          :rules="[v => !!v || 'Ethnicity is required']"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Ethnicity
          </template>
        ></v-select>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Current Photo -->
        <!-- the "rules" checks that the image size is less than 2 MB -->
        <v-file-input
          accept="image/png, image/jpeg, image/bmp"
          :rules="[(v) => (v ? v.size : 0) < 2097152 || 'Image size should be less than 2 MB!']"
          placeholder="Upload a recent photo"
          v-model="patientPhoto"
          label="Photo"
          prepend-icon="mdi-camera"
        ></v-file-input>
      </v-col>
    </v-row>
  </v-container>
</v-form>
</template>
<script>
import EventBus from "../eventBus";

export default {
  data() {
    return {
      suffixOptions: ["II", "III", "IV", "Jr", "Sr"],
      genderIdOptions: ["Male", "Female", "Other", "Decline to answer"],
      raceOptions: [
        "Black or African American",
        "White",
        "Asian",
        "American Indian or Alaska Native",
        "Native Hawaiian or other Pacific Islander",
        "Other",
      ],
      ethnicityOptions: [
        "Hispanic or Latino",
        "Not Hispanic or Latino",
        "Unknown or prefer not to answer",
      ],
      familyName: "",
      givenName: "",
      middleName: "",
      suffix: "",
      dob: "",
      gender: "",
      patientPhoto: [],
      race: "",
      ethnicity: "",
      preferredLanguage: "",
      minDateStr: "1900-01-01",
      birthdateRules: [
        (v) => !!v || "DOB is required",
        // check if v exists before seeing if the length is 10
        (v) => !!v && v.length === 10 || "DOB must be in specified format, MM/DD/YYYY",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
      valid: false,
    };
  },
  computed: {
    maxDateStr: function() {
      let d = new Date();
      let date = [
        d.getFullYear(),
        ("0" + (d.getMonth() + 1)).slice(-2),
        ("0" + d.getDate()).slice(-2),
        ].join("-");

      return date;
    },
  },
  methods: {
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var formattedDate = () => {
        // Ensure birthdate is fully entered and can be converted into 3 variables
        if(birthdate) {
          if(birthdate.split('/').length === 3) {
            const [month, day, year] = birthdate.split('/');
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
          }
        }
        return false;
      }
      var date = Date.parse(formattedDate());

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    parseDate (date) {
      if (!date) return null;
      // Ensure birthdate is fully entered and can be converted into 3 variables before parsing
      if (date.split('/').length !== 3) return null;

      const [month, day, year] = date.split('/');
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    },
    sendPersonalInfoDataToReviewPage() {
      const personalInfoPayload = {
        familyName: this.familyName,
        givenName: this.givenName,
        middleName: this.middleName,
        suffix: this.suffix,
        birthDate: this.dob,
        gender: this.gender,
        patientPhoto: this.patientPhoto,
        patientPhotoSrc:  (this.patientPhoto && this.patientPhoto.size) ? URL.createObjectURL( this.patientPhoto ) : undefined,
        race: this.race,
        ethnicity: this.ethnicity,
        preferredLanguage: this.preferredLanguage,
      };
      EventBus.$emit("DATA_PERSONAL_INFO_PUBLISHED", personalInfoPayload);
    },
    verifyFormContents() {

      //add logic to check form contents
      var valid = true;
      var message = "Woops! You need to enter the following field(s):";

      if (this.familyName == "") {
        message += " Last Name";
        valid = false;
      }
      if (this.givenName == "") {
        if (!valid) {
          message += ",";
        }
        message += " First Name";
        valid = false;
      }
      if (this.dob == "") {
        if (!valid) {
          message += ",";
        }
        message += " Date of Birth";
        valid = false;
      }
      if (this.gender == "") {
        if (!valid) {
          message += ",";
        }
        message += " Gender Identity";
        valid = false;
      }
      if (this.patientPhoto && this.patientPhoto.size > 2097152) {
        if (!valid) {
          message += "\n";
          message += "Your selected photo is too large. Please resubmit one under 2MBs.";
        }
        else {
          message = "Your selected photo is too large. Please resubmit one under 2MBs.";
        }
        valid = false;
      }
      if (this.race == "") {
        if (!valid) {
          message += ",";
        }
        message+= " Race"
        valid = false
      }
      if (this.ethnicity == "") {
        if (!valid) {
            message += ",";
        }
        message += " Ethnicity"
        valid = false
      }
      if (valid == false) {
        alert(message);
        return false;
      }
        this.$refs.form.validate();
        if (!this.valid) return;
      this.sendPersonalInfoDataToReviewPage();
      return true;
    },
  },
  mounted() {
    EventBus.$on("DATA_LANGUAGE_INFO_PUBLISHED", (preferredLanguage) => {
      this.preferredLanguage = preferredLanguage;
    });
  },
};
</script>

