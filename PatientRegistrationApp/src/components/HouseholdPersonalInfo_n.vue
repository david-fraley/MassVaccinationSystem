<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5">
        <!-- Language -->
        <v-select
          :items="languageOptions"
          label="Preferred language"
          required
          :rules="[(v) => !!v || 'Preferred language field is required']"
          v-model="preferredLanguage"
          prepend-icon="mdi-menu-right"
        ></v-select>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
          id="lastName"
          required
          :rules="[(v) => !!v || 'Last name field is required']"
          v-model="householdFamilyName"
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
          v-model="householdGivenName"
        >
        </v-text-field>
      </v-col>

      <!-- Suffix -->
      <v-col class="d-flex" cols="2" sm="2">
        <v-text-field label="Suffix" id="suffix" v-model="householdSuffix">
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
              v-model="householdDate"
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
            v-model="householdDate"
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
          v-model="householdGender"
          prepend-icon="mdi-menu-right"
        ></v-select>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5">
        <!-- Relationship -->
        <v-select
          :items="relationshipOptions"
          label="Relationship: this person is your"
          required
          :rules="[(v) => !!v || 'Relationship field is required']"
          v-model="relationship"
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
          :rules="[(v) => v.size < 2097152 || 'Image size should be less than 2 MB!']"
          placeholder="Upload a recent photo"
          v-model="householdPatientPhoto"
          label="Photo"
          prepend-icon="mdi-camera"
        ></v-file-input>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Race -->
        <v-select
          v-model="householdRaceSelections"
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
          v-model="householdEthnicitySelection"
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
      householdFamilyName: "",
      householdGivenName: "",
      householdSuffix: "",
      householdDate: "",
      householdGender: "",
      householdPatientPhoto: "",
      householdRaceSelections: "",
      householdEthnicitySelection: "",
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
        householdFamilyName: this.householdFamilyName,
        householdGivenName: this.householdGivenName,
        householdSuffix: this.householdSuffix,
        householdBirthDate: this.householdDate,
        householdGender: this.householdGender,
        householdPatientPhoto: this.householdPatientPhoto,
        householdPatientPhotoSrc: URL.createObjectURL( this.householdPatientPhoto ),
        householdRaceSelections: this.householdRaceSelections,
        householdEthnicitySelection: this.householdEthnicitySelection,
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

      if (this.householdFamilyName == "") {
        if (!valid) {
          message += ",";
        }
        message += " Last Name";
        valid = false;
      }

      if (this.householdGivenName == "") {
        if (!valid) {
          message += ",";
        }
        message += " First Name";
        valid = false;
      }

      if (this.householdDate == null) {
        if (!valid) {
          message += ",";
        }
        message += " Date of birth";
        valid = false;
      }

      if (this.householdGender == "") {
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

      if( this.householdPatientPhoto ) {
        if (this.householdPatientPhoto.size > 2097152) {
          if (!valid) {
            message += "\n";
            message += "The selected photo file size is above 2MB. Please upload a file less than 2MB.";
          }
          else {
            message = "The selected photo file size is above 2MB. Please upload a file less than 2MB.";
          }
          valid = false;
        }
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
