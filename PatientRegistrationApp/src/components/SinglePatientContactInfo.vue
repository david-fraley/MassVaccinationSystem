<template>
	<v-container>
    <v-row>
      <v-col cols="12">
        <div>We ask that you please provide your phone number and/or e-mail address.  This contact information will provide us with the ability to send you important information regarding your vaccination registration, follow-up appointment, and vaccination summary.</div>  
        <div><br>By withholding your e-mail and/or phone number, it will not be possible to provide you with the important vaccination information.</div>
        <div><strong>We strongly encourage you to provide your e-mail and/or phone number as contact information.</strong></div>
        <div><br></div>
        <v-divider></v-divider>
        <div><br>By providing your e-mail and/or phone number as contact information, you agree to the following:</div>
        <div><strong>You understand that we will use the phone number and e-mail that you provide to contact you with vaccination registration and follow-up information.</strong></div>
      </v-col>
    </v-row> 
    <v-row no-gutters>
      <v-checkbox
        v-model="acknowledgement">
        <template #label>
          <span class="red--text"><strong>* <br></strong></span>I have read and understood the above.
        </template>
      </v-checkbox>
    </v-row>
    <v-row no-gutters>
      <v-checkbox
        v-model="permission"
        :disabled="!acknowledgement"
        label="I agree to provide and authorize the use of my contact information as outlined above."
        >
      </v-checkbox>
    </v-row>
    <v-divider></v-divider>
    <v-row align="center" justify="start">
      <v-col cols="4">
        <v-text-field
          v-model="patientPhoneNumber"
          :disabled="!permission"
          :rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
          v-mask="'(###)###-####'"
          prepend-icon="mdi-phone"
          label="Phone Number">
        </v-text-field>
      </v-col>
      <v-col cols="3">
        <v-select
          v-model="patientPhoneNumberType"
          :disabled="!permission"
          :items="phoneTypeOptions"
          label="Phone Type"
        ></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4">
        <v-text-field
          v-model="patientEmail"
          :disabled="!permission"
          :rules="emailRules"
          prepend-icon="mdi-email"
          label="E-mail Address">
        </v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <p></p>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  name: "SinglePatientContactInfo",
  data() {
    return {
      phoneTypeOptions: ["Home", "Mobile", "Work"],
      emailRules: [
        (v) =>
          /^[\s]*$|.+@.+\..+/.test(v) ||
          "Please provide a valid e-mail address",
      ],
      patientPhoneNumber: '',
      patientPhoneNumberType: '',
      patientEmail: '',
      acknowledgement: false,
      permission: false,
    };
  },
  methods: {
    sendContactInfoInfoToReviewPage()
    {
      const contactInfoPayload = {
        patientPhoneNumber: this.patientPhoneNumber,
        patientPhoneNumberType: this.patientPhoneNumberType,
        patientEmail: this.patientEmail,
      }
      EventBus.$emit('DATA_CONTACT_INFO_PUBLISHED', contactInfoPayload)
    },
    verifyFormContents()
    {
      var valid = true
      var message
	
      if(this.acknowledgement)
      {	
        if(this.permission)
        {
          if((this.patientPhoneNumber == "") && (this.patientEmail == ""))
            {
              message = "Please provide an e-mail address and/or phone number."
              valid = false
            }
        }
      }
      else
      {
        message = "Acknowledgement is required."
        valid = false
      }
      
			if (valid == false) 
			{
				alert (message)
				return false
			}
	
      this.sendContactInfoInfoToReviewPage();
      return true;
    },
  },
}
</script>
