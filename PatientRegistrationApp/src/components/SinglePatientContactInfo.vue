<template>
	<v-container fluid>
    <v-row>
     <v-radio-group
        required
        :rules="[v => !!v || 'This field is required']"
        v-model="phoneNumberAvailableRadioButtons"
      >
        <v-col align="right" cols="12">
          <v-radio label="I have a phone number" value="yes" @change="PhoneNumberAvailable()"></v-radio>
          <v-radio label="I do not have a phone number" value="no" @change="PhoneNumberNotAvailable()"></v-radio>
        </v-col>
      </v-radio-group>
    </v-row>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <v-text-field
          required
          :rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
          placeholder="(###)###-####"
          v-mask="'(###)###-####'"
          v-model="patientPhoneNumber"
          v-show="phoneNumberAvailable"
          prepend-icon="mdi-menu-right">
			<template #label>
			<span class="red--text"><strong>* </strong></span>Phone Number
			</template>
        </v-text-field>
	</v-col>

	<v-col class="d-flex" cols="6" sm="2">
        <v-select
          required
          :rules="[v => !!v || 'Phone type is required']"
          v-model="patientPhoneNumberType"
          :items="phonetype"
          label="Phone Type"
          v-show="phoneNumberAvailable"
        ></v-select>
	</v-col>
    </v-row>

    <v-row>
	<v-radio-group
        required
        :rules="[v => !!v || 'This field is required']"
        v-model="emailAvailableRadioButtons"
	>
        <v-col align="right" cols="12">
          <v-radio label="I have an E-mail address" value="yes" @change="EmailAvailable()"></v-radio>
          <v-radio label="I do not have an E-mail address" value="no" @change="EmailNotAvailable()"></v-radio>
        </v-col>
	</v-radio-group>
    </v-row>
	
    <v-row>
	<v-col cols="12" sm="6" md="6">
        <v-text-field
			required
			:rules="emailRules"
			v-model="patientEmail"
			v-show="emailAvailable"
			prepend-icon="mdi-menu-right">
				<template #label>
				<span class="red--text"><strong>* </strong></span>E-mail Address
				</template>
        </v-text-field>
	</v-col>
    </v-row>
	


    <v-row><v-col cols="12" sm="6" md="6">
      <v-radio-group
        required
        :rules="[v => !!v || 'This field is required']"
        v-model="approval"
		prepend-icon="mdi-menu-right">
			<template #label>
			<span class="red--text"><strong>* </strong></span>May we contact you regarding follow up vaccination information
			</template>
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
      phonetype: ["Home", "Mobile", "Work"],
      radios: "May we contact you for a follow up vaccination?",
      emailRules: [
        (v) =>
          /^[\s]*$|.+@.+\..+/.test(v) ||
          "Please provide a valid e-mail address",
      ],
	patientPhoneNumber: '',
	patientPhoneNumberType: '',
	patientEmail: '',
	approval: '',
	phoneNumberAvailable: true,
	phoneNumberAvailableRadioButtons: 'yes',
	emailAvailable: true,
	emailAvailableRadioButtons: 'yes'
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
      var message = "Woops! You need to enter the following field(s):"
	
      if(this.phoneNumberAvailable)
      {	
        if(this.patientPhoneNumber == "") 
        {
          message += " Phone Number"
          valid = false
        }
      }
	
      if(this.emailAvailable)
      {
        if(this.patientEmail == "") 
        {
        if(!valid)
          {
          message +=","
          }
          message += " E-mail"
          valid = false
        }
      }
			if(this.approval == "") 
			{
        if(!valid)
				{
          message +=","
				}
        message += " Follow up consent"
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
	PhoneNumberAvailable()
    {
		this.phoneNumberAvailable = true
		this.patientPhoneNumber=""
    },
    PhoneNumberNotAvailable()
    {
		this.phoneNumberAvailable = false
		this.patientPhoneNumber="Not available"
    },
		EmailAvailable()
    {
	this.emailAvailable = true
	this.patientEmail=""
    },
    EmailNotAvailable()
    {
	this.emailAvailable = false
	this.patientEmail="Not available"
    },
},
}
</script>
