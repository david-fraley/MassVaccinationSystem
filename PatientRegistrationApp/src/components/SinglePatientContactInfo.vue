<template>
  <v-container fluid>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <v-text-field
			required
			:rules="[v => !!v || 'Phone number is required']"
			label="Phone Number"
			v-model="patientPhoneNumber"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <v-col class="d-flex" cols="6" sm="2">
        <v-select
          required
          :rules="[v => !!v || 'Phone type is required']"
          v-model="patientPhoneNumberType"
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
			required
			:rules="emailRules"
			label="E-mail Address"
			v-model="patientEmail"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>
    </v-row>
	
	<v-row>
    <v-col cols="6" sm="6" md="3">
      <v-checkbox
        v-model="checkbox"
        label="I have no e-mail address"
      ></v-checkbox>
    </v-col></v-row>
	

    <v-row><v-col cols="12" sm="6" md="6">
      <v-radio-group
        required
        :rules="[v => !!v || 'This field is required']"
        v-model="approval"
		prepend-icon="mdi-menu-right"
        label="May we contact you regarding follow up vaccination information?"
      >
        <v-col align="right" cols="3" sm="3" md="3">
		
          <v-radio label="Yes" value="yes"></v-radio>
          <v-radio label="No" value="no"></v-radio>
        </v-col>
      </v-radio-group></v-col>
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
          "Please provide a valid e-mail address",
      ],
      patientPhoneNumber: '',
      patientPhoneNumberType: '',
      patientEmail: '',
      approval: ''
    };
  },
  methods: {
    sendContactInfoInfoToReviewPage()
    {
      const contactInfoPayload = {
        patientPhoneNumber: this.patientPhoneNumber,
        patientPhoneNumberType: this.patientPhoneNumberType,
        patientEmail: this.patientEmail,
        approval: this.approval
      }
      EventBus.$emit('DATA_CONTACT_INFO_PUBLISHED', contactInfoPayload)
    },
    verifyFormContents()
    {
      //add logic to check form contents
      var valid = true
      var message = "Woops! You need to enter the following fields:"
	
			if(this.patientPhoneNumber == "") {
        message += "*Phone Number"
        valid = false
			}
			if(this.patientEmail == "") {
        message += "*E-mail"
        valid = false
			}
			if(this.approval == "") {
        message += "*Follow up consent"
        valid = false
			}
			if (valid == false) {
				alert (message)
				return false
			}
	
      this.sendContactInfoInfoToReviewPage();
      return true;
    }
  },
}
</script>
