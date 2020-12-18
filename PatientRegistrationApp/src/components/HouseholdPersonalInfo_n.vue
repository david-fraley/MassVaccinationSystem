<template>
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
        <v-text-field 
          label="Suffix" 
          v-model="suffix"
          prepend-icon="mdi-blank">
        </v-text-field>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Language -->
        <v-select
          :items="languageOptions"
          required
          :rules="[v => !!v || 'Preferred language field is required']"
          v-model="preferredLanguage"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Preferred Language
          </template>
        </v-select>
      </v-col>
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Relationship -->
        <v-select
          :items="relationshipOptions"
          required
          :rules="[v => !!v || 'Relationship field is required']"
          v-model="relationship"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Relationship: this person is your
          </template>
        </v-select>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Date of Birth -->
        <v-menu
          attach
          :close-on-content-click="false"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="birthDate"
              :rules="birthdateRules"
              placeholder="YYYY-MM-DD"
              v-mask="'####-##-##'"
              prepend-icon="mdi-calendar"
              @click:prepend="on.click"
              >
              <template #label>
                <span class="red--text"><strong>* </strong></span>Date of Birth
              </template>
            </v-text-field>
          </template>
          <v-date-picker
            reactive
            v-model="birthDate"
            :min="minDateStr"
            :max="maxDateStr"
          ></v-date-picker>
        </v-menu>
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
          label="Race (select all that apply)"
          prepend-icon="mdi-blank"
          multiple
        ></v-select>
      </v-col>
      <v-col cols="12" sm="6" md="6" lg="4">
        <!-- Ethnicity -->
        <v-select
          v-model="ethnicity"
          :items="ethnicityOptions"
          label="Ethnicity"
          prepend-icon="mdi-blank"
        ></v-select>
      </v-col>
    </v-row>
    <v-row align="left" justify="start">
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
</template>

<script>
import EventBus from "../eventBus";

export default {
  data() {
    return {
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
      languageOptions: ["English", "Spanish"],
      relationshipOptions: [
        "Child",
        "Foster Child",
        "Spouse",
        "Domestic Partner",
        "Parent",
        "In-Law",
        "Grandparent",
        "Other Family Member",
      ],
      familyName: "",
      givenName: "",
      middleName: "",
      suffix: "",
      birthDate: "",
      gender: "",
      patientPhoto: [],
      race: "",
      ethnicity: "",
      preferredLanguage: "",
      relationship: "",
      minDateStr: "1900-01-01",
      birthdateRules: [
        (v) => !!v || "DOB is required",
        (v) => v.length == 10 || "DOB must be in specified format",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
    };
  },
  props: {
    householdMemberNumber: Number,
  },
  methods: {
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var date = Date.parse(birthdate);

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    sendHouseholdPersonalInfoDataToReviewPage() {
      const householdPersonalInfoPayload = {
        preferredLanguage: this.preferredLanguage,
        familyName: this.familyName,
        givenName: this.givenName,
        middleName: this.middleName,
        suffix: this.suffix,
        birthDate: this.birthDate,
        gender: this.gender,
        patientPhoto: this.patientPhoto,
        patientPhotoSrc: (this.patientPhoto && this.patientPhoto.size) ? URL.createObjectURL( this.patientPhoto ) : undefined,
        race: this.race,
        ethnicity: this.ethnicity,
      };
      EventBus.$emit(
        "DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED",
        householdPersonalInfoPayload,
        this.householdMemberNumber
      );
    },
    verifyFormContents() {
      var valid = true;
      var message = "Woops! You need to enter the following field(s):";

      if (this.preferredLanguage == "") {
        message += " Preferred Language";
        valid = false;
      }
      if (this.familyName == "") {
        if (!valid) {
          message += ",";
        }
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
      if (this.birthDate == null) {
        if (!valid) {
          message += ",";
        }
        message += " Date of birth";
        valid = false;
      }
      if (this.gender == "") {
        if (!valid) {
          message += ",";
        }
        message += " Gender Identity";
        valid = false;
      }
      if (this.relationship == "") {
        if (!valid) {
          message += ",";
        }
        message += " Relationship";
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
      if (valid == false) {
        alert(message);
        return false;
      }

      this.sendHouseholdPersonalInfoDataToReviewPage();
      return true;
    },
    setHouseholdFamilyName(householdFamilyName) {
      this.householdFamilyName = householdFamilyName;
    },
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
};
</script>

