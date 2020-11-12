<template>
	<v-container fluid>
	<v-row align="center" justify="center">
		<!-- Last name -->
		<v-col class="d-flex" cols="5" sm="5">
        <v-text-field
			label="Last Name"
			required
			:rules="[v => !!v || 'Last name field is required']"
			v-model="householdEmergencyContactFamilyName"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
		</v-col>

		<!-- First name -->
		<v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
			label="First Name"
			required
			:rules="[v => !!v || 'First name field is required']"
			v-model="householdEmergencyContactGivenName">
        </v-text-field>
		</v-col>
    </v-row>

    <v-row align="center" justify="center">
		<!-- Phone Number -->
		<v-col class="d-flex" cols="5" sm="5">
        <v-text-field
			label="Phone Number"
			required
			:rules="[v => !!v || 'Phone number field is required']"
			v-model="householdEmergencyContactPhoneNumber"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
		</v-col>

		<!-- Phone Number Type -->
		<v-col class="d-flex" cols="5" sm="3">
        <v-select
			v-model="householdEmergencyContactPhoneNumberType"
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
  data() {
    return {
      phoneType: ["Cell", "Home"],
      householdEmergencyContactFamilyName: '',
      householdEmergencyContactGivenName: '',
      householdEmergencyContactPhoneNumber: '',
      householdEmergencyContactPhoneNumberType: ''
    };
  },
  methods: {
    sendHouseholdEmergencyContactInfoToReviewPage()
    {
      const householdEmergencyContactPayload = {
        householdEmergencyContactFamilyName: this.householdEmergencyContactFamilyName,
        householdEmergencyContactGivenName: this.householdEmergencyContactGivenName,
        householdEmergencyContactPhoneNumber: this.householdEmergencyContactPhoneNumber,
        householdEmergencyContactPhoneNumberType: this.householdEmergencyContactPhoneNumberType
      }
      EventBus.$emit('DATA_HOUSEHOLD_EMERGENCY_CONTACT_INFO_PUBLISHED', householdEmergencyContactPayload)
    },
    verifyFormContents()
    {
      //add logic to check form contents
      var valid = true
      var message = "Woops! You need to enter the following field(s):"
      
	if(this.householdEmergencyContactFamilyName == "")  
	{
		message += " Last Name"
		valid = false
	}
      
	if(this.householdEmergencyContactGivenName == "") 
	{
	if(!valid)
				{
				message +=","
				}
        message += " First Name"
        valid = false
	}
      
	if (this.householdEmergencyContactPhoneNumber == "") 
	{
	if(!valid)
				{
				message +=","
				}
        message += " Phone Number"
        valid = false
	}
      
	if (valid == false) 
		{
		alert(message)
		return false
        }
	this.sendHouseholdEmergencyContactInfoToReviewPage();
	return true;
    }
},
};
</script>
