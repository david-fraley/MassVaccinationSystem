<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
          v-model="contactFamilyName"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
          label="First Name"
          v-model="contactGivenName">
        </v-text-field>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <!-- Phone Number -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Phone Number"
          v-model="contactPhoneNumber"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="5" sm="3">
        <v-select
          v-model="contactPhoneNumberType"
          :items="phoneType"
          label="Phone Number Type"
        ></v-select>
      </v-col>
      <v-col class="d-flex" cols="5" sm="2"> </v-col>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  name: "SinglePatientEmergencyContact",
  data() {
    return {
      phoneType: ["Cell", "Home"],
      contactFamilyName: '',
      contactGivenName: '',
      contactPhoneNumber: '',
      contactPhoneNumberType: ''
    };
  },
  methods: {
    sendEmergencyContactInfoToReviewPage()
    {
      const emergencyContactPayload = {
        contactFamilyName: this.contactFamilyName,
        contactGivenName: this.contactGivenName,
        contactPhoneNumber: this.contactPhoneNumber,
        contactPhoneNumberType: this.contactPhoneNumberType
      }
      EventBus.$emit('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', emergencyContactPayload)
    },
    verifyFormContents()
    {
      //add logic to check form contents
      this.sendEmergencyContactInfoToReviewPage();
      return true;
    }
  },
};
</script>
