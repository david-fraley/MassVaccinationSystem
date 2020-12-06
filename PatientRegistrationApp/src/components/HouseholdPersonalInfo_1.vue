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

    <v-row>
      <v-col cols="12" sm="12" md="12">
        <v-checkbox
          v-model="checkbox"
          label="All household members have the same last name"
        ></v-checkbox>
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
      householdFamilyName: "",
      householdGivenName: "",
      householdSuffix: "",
      householdDate: "",
      householdGender: "",
      householdPatientPhoto: "",
      householdRaceSelections: "",
      householdEthnicitySelection: "",
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
    sendHouseholdPersonalInfoDataToReviewPage() {
      const householdPersonalInfoPayload = {
        householdFamilyName: this.householdFamilyName,
        householdGivenName: this.householdGivenName,
        householdSuffix: this.householdSuffix,
        householdBirthDate: this.householdDate,
        householdGender: this.householdGender,
        householdPatientPhoto: this.householdPatientPhoto,
        householdPatientPhotoSrc: this.householdPatientPhoto  ? URL.createObjectURL( this.householdPatientPhoto ) : undefined,
        householdRaceSelections: this.householdRaceSelections,
        householdEthnicitySelection: this.householdEthnicitySelection,
        preferredLanguage: this.preferredLanguage,
      };
      const householdMemberNumber = 1;
      EventBus.$emit(
        "DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED",
        householdPersonalInfoPayload,
        householdMemberNumber
      );
      if (this.checkbox) {
        //all household members have the same last name
        EventBus.$emit("DATA_HOUSEHOLD_FAMILY_NAME", this.householdFamilyName);
      } else {
        EventBus.$emit("DATA_HOUSEHOLD_FAMILY_NAME", "");
      }
    },
    verifyFormContents() {
      //add logic to check form contents
      var valid = true;
      var message = "Woops! You need to enter the following field(s):";

      if (this.householdFamilyName == "") {
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
 
      if( this.householdPatientPhoto ) {
        if (this.householdPatientPhoto.size > 2097152) {
          if (!valid) {
            message += "\n";
            message += "Your selected photo is too large. Please resubmit one under 2MBs.";
          }
          else {
            message = "Your selected photo is too large. Please resubmit one under 2MBs.";
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
