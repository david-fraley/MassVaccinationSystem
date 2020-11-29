<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
          id="lastName"
          required
          :rules="[(v) => !!v || 'Last name field is required']"
          v-model="familyName"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="First Name"
          id="firstName"
          required
          :rules="[(v) => !!v || 'First name field is required']"
          v-model="givenName"
        >
        </v-text-field>
      </v-col>

      <!-- Suffix -->
      <v-col class="d-flex" cols="2" sm="2">
        <v-text-field label="Suffix" id="suffix" v-model="suffix">
        </v-text-field>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
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
              v-model="date"
              label="Date of Birth"
              :rules="birthdateRules"
              placeholder="YYYY-MM-DD"
              v-mask="'####-##-##'"
              prepend-icon="mdi-calendar"
              @click:prepend="on.click"
            ></v-text-field>
          </template>

          <v-date-picker
            reactive
            v-model="date"
            :min="minDateStr"
            :max="maxDateStr"
          ></v-date-picker>
        </v-menu>
      </v-col>

      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Gender identity -->
        <v-select
          :items="genderID"
          label="Gender identity"
          required
          :rules="[(v) => !!v || 'Gender identity field is required']"
          v-model="gender"
          prepend-icon="mdi-menu-right"
        ></v-select>
      </v-col>

      <v-spacer></v-spacer>
    </v-row>

    <v-row align="left" justify="left">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Current Photo -->
        <v-file-input
          accept="image/png, image/jpeg, image/bmp"
          :rules="[(v) => !v || v.size < 2097152 || 'Image size should be less than 2 MB!']"
          placeholder="Upload a recent photo"
          v-model="patientPhoto"
          label="Photo"
          prepend-icon="mdi-camera"
        ></v-file-input>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Race -->
        <v-select
          v-model="raceSelections"
          :items="race"
          label="Race (select all that apply)"
          prepend-icon="mdi-menu-right"
          multiple
        ></v-select>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Ethnicity -->
        <v-select
          v-model="ethnicitySelection"
          :items="ethnicity"
          label="Ethnicity"
          prepend-icon="mdi-menu-right"
        ></v-select>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from "../eventBus";

export default {
  data() {
    return {
      genderID: ["Male", "Female", "Other", "Decline to answer"],
      race: [
        "Black or African American",
        "White",
        "Asian",
        "American Indian or Alask a Native",
        "Native Hawaiian or other Pacific Islander",
        "Other",
      ],
      ethnicity: [
        "Hispanic or Latino",
        "Not Hispanic or Latino",
        "Unknown or prefer not to answer",
      ],
      familyName: "",
      givenName: "",
      suffix: "",
      date: "",
      gender: "",
      patientPhoto: undefined,
      raceSelections: "",
      ethnicitySelection: "",
      preferredLanguage: "",
      minDateStr: "1900-01-01",
      birthdateRules: [
        (v) => !!v || "DOB is required",
        (v) => v.length == 10 || "DOB must be in specified format",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
    };
  },
  methods: {
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var date = Date.parse(birthdate);

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    sendPersonalInfoDataToReviewPage() {
      const personalInfoPayload = {
        familyName: this.familyName,
        givenName: this.givenName,
        suffix: this.suffix,
        birthDate: this.date,
        gender: this.gender,
        patientPhoto: this.patientPhoto,
        patientPhotoSrc:  this.patientPhoto  ? URL.createObjectURL( this.patientPhoto ) : undefined,
        raceSelections: this.raceSelections,
        ethnicitySelection: this.ethnicitySelection,
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

      if (this.date == null) {
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

      if( this.patientPhoto ) {
        if (this.patientPhoto.size > 2097152) {
          if (!valid) {
            message += "\n";
            message += "Photo is over 2mb's";
          }
          else {
            message = "Photo is over 2mb's";
          }
          valid = false;
        }
      }

      if (valid == false) {
        alert(message);
        return false;
      }

      this.sendPersonalInfoDataToReviewPage();
      return true;
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
  mounted() {
    EventBus.$on("DATA_LANGUAGE_INFO_PUBLISHED", (preferredLanguage) => {
      this.preferredLanguage = preferredLanguage;
    });
  },
};
</script>
