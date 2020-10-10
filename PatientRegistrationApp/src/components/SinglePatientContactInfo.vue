
<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <v-text-field
          :rules="['Required']"
          label="Primary Phone Number"
          v-model="contactPhoneNumber"
        ></v-text-field>
      </v-col>

      <v-col class="d-flex" cols="6" sm="2">
        <v-select
          :rules="['Required']"
          v-model="contactPhoneNumberType"
          :items="phonetype"
          label="Phone Type"
        ></v-select>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="6" sm="6" md="3">
        <v-checkbox
          v-model="checkbox"
          label="I have no phone number"
        ></v-checkbox>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" md="6">
        <v-text-field
          :rules="emailRules"
          label="Primary Email Address"
          v-model="contactEMail"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-col cols="12" sm="6" md="3">
      <v-checkbox
        v-model="checkbox"
        label="I have no email address"
      ></v-checkbox>
    </v-col>

    <v-row>
      <v-radio-group
        v-model="contactApproval"
        label="May we contact you regarding follow up vaccination information?"
      >
        <v-col align="right" cols="3" sm="3" md="3">
          <v-radio label="Yes" value="yes"></v-radio>
          <v-radio label="No" value="no"></v-radio>
        </v-col>
      </v-radio-group>
    </v-row>
    <v-row>
    <v-col cols="12" >
        <v-btn
            large
            color="secondary"
            @click="sendContactInfoInfoToReviewPage"
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
  name: "SinglePatientContactInfo",
  data() {
    return {
      phonetype: ["Cell", "Landline", "Other"],
      radios: "May we contact you for a follow up vaccination?",
      emailRules: [
        (v) =>
          /^[\s]*$|.+@.+\..+/.test(v) ||
          "E-mail must be in the format example@example.com",
      ],
    };
  },
  methods: {
    sendContactInfoInfoToReviewPage()
    {
      const contactInfoPayload = {
        contactPhoneNumber: this.contactPhoneNumber,
        contactPhoneNumberType: this.contactPhoneNumberType,
        contactEMail: this.contactEMail,
        contactApproval: this.contactApproval
      }
      EventBus.$emit('DATA_CONTACT_INFO_PUBLISHED', contactInfoPayload)
    }
  }
};
</script>
