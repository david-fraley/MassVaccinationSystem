<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field label="Last Name" id="lastName" v-model="lName"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field label="First Name" id="firstName" v-model="fName"></v-text-field>
      </v-col>

      <!-- Suffix -->
      <v-col class="d-flex" cols="2" sm="2">
        <v-text-field label="Suffix" id="suffix" v-model="suffix"></v-text-field>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Date of Birth -->
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="date"
              label="Date of Birth"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>

          <v-date-picker
            ref="picker"
            v-model="date"
            :max="new Date().toISOString().substr(0, 10)"
            min="1900-01-01"
            @change="save"
          ></v-date-picker>
        </v-menu>
      </v-col>

      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="5" sm="5">
        <!-- Gender identity -->
        <v-select
          :items="gender"
          label="Gender identity"
          v-model="genderID"
          prepend-icon="mdi-menu-right"
        ></v-select>
      </v-col>

      <v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
      <v-col class="d-flex" cols="12" sm="12">
        <!-- Current Photo -->
        <v-file-input
          :rules="rules"
          accept="image/png, image/jpeg, image/bmp"
          placeholder="Upload a recent photo"
          v-model="recentPhoto"
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
import EventBus from '../eventBus'

export default {
  data() {
    return {
      gender: ["Male", "Female", "Other", "Decline to answer"],
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
      lName: '',
      fName: '',
      suffix: '',
      dateOfBirth: '',
      genderID: '',
      recentPhoto: '',
      raceSelections: '',
      ethnicitySelection: ''
    };
  },
  methods: {
    sendPersonalInfoDataToReviewPage()
    {
      const personalInfoPayload = {
        lName: this.lName,
        fName: this.fName,
        suffix: this.suffix,
        dateOfBirth: this.date,
        genderID: this.genderID,
        recentPhoto: this.recentPhoto,
        raceSelections: this.raceSelections,
        ethnicitySelection: this.ethnicitySelection
      }
      EventBus.$emit('DATA_PERSONAL_INFO_PUBLISHED', personalInfoPayload)
    },
    verifyFormContents()
    {
      //add logic to check form contents
      this.sendPersonalInfoDataToReviewPage();
      return true;
    }
  },
};
</script>
