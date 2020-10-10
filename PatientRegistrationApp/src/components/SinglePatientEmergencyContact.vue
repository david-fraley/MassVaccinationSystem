
<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
          v-model="eContactLastName"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>
      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
          label="First Name"
          v-model="eContactFirstName">
        </v-text-field>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <!-- Phone Number -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Phone Number"
          v-model="eContactPhoneNumber"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>
      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="5" sm="3">
        <v-select
          v-model="eContactPhoneNumberType"
          :items="phoneType"
          label="Phone Number Type"
        ></v-select>
      </v-col>
      <v-col class="d-flex" cols="5" sm="2"> </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" >
        <v-btn
            large
            color="secondary"
            @click="sendEmergencyContactInfoToReviewPage"
          >
            Send data
          </v-btn>
      </v-col>
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
    };
  },
  methods: {
    sendEmergencyContactInfoToReviewPage()
    {
      const emergencyContactPayload = {
        eContactLastName: this.eContactLastName,
        eContactFirstName: this.eContactFirstName,
        eContactPhoneNumber: this.eContactPhoneNumber,
        eContactPhoneNumberType: this.eContactPhoneNumberType
      }
      EventBus.$emit('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', emergencyContactPayload)
    }
  }

};
</script>
