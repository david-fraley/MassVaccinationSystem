<template>
	<v-container fluid>
		<v-row>
      <!-- Last name -->
      <v-col class="d-flex" cols="6" sm="6">
        <v-text-field
		required
          :rules="[v => !!v || 'Last name field is required']"
          v-model="householdEmergencyContactFamilyName"
			prepend-icon="mdi-menu-right">
        <template #label>
				<span class="red--text"><strong>* </strong></span>Last Name
				</template>
        </v-text-field>
		</v-col>

      <!-- First name -->
      <v-col class="d-flex" cols="6" sm="6">
        <v-text-field 
			required
			:rules="[v => !!v || 'First name field is required']"
			v-model="householdEmergencyContactGivenName">
        <template #label>
				<span class="red--text"><strong>* </strong></span>First Name
				</template>
        </v-text-field>
		</v-col>
    </v-row>

    <v-row>
      <!-- Phone Number -->
      <v-col class="d-flex" cols="12" sm="6">
        <v-text-field
          label="Phone Number"
          required
          :rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
          v-mask="'(###)###-####'"
          v-model="householdEmergencyContactPhoneNumber"
			prepend-icon="mdi-menu-right">
        <template #label>
				<span class="red--text"><strong>* </strong></span>Phone Number
				</template>
        </v-text-field>
		</v-col>

      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="6" sm="3">
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
      phoneType: ["Home", "Mobile", "Work"],
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


