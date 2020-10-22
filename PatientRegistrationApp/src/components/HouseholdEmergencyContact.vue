<template>
	<v-container fluid>
	
		<v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
			required
			:rules="[v => !!v || 'Last name field is required']"
          v-model="emergencyContactFamilyName"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
          label="First Name"
			required
			:rules="[v => !!v || 'First name field is required']"
          v-model="emergencyContactGivenName">
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
          v-model="emergencyContactPhoneNumber"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>

      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="5" sm="3">
        <v-select
          v-model="emergencyContactPhoneNumberType"
          :items="phoneType"
          label="Phone Number Type"
        ></v-select>
      </v-col>
      <v-col class="d-flex" cols="5" sm="2"> </v-col>
    </v-row>
  </v-container>
</template>

<script>
//import EventBus from '../eventBus'

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
    /*sendHouseholdEmergencyContactInfoToHouseholdReviewPage()
    {
      const emergencyContactPayload = {
        emergencyContactFamilyName: this.householdEmergencyContactFamilyName,
        emergencyContactGivenName: this.householdEmergencyContactGivenName,
        emergencyContactPhoneNumber: this.householdEmergencyContactPhoneNumber,
        emergencyContactPhoneNumberType: this.householdEmergencyContactPhoneNumberType
      }
      EventBus.$emit('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', emergencyContactPayload)
    },*/
    verifyFormContents()
    {
		//add logic to check form contents
		var valid = true
		var message = "Woops! You need to enter the following fields:"
		
		if(this.householdEmergencyContactFamilyName == "")  {
			message += " *Last Name"
			valid = false
		}
		
		if(this.householdEmergencyContactGivenName == "") {
			message += " *First Name"
			valid = false
		}
		
		
		if (this.householdEmergencyContactPhoneNumber == "") {
			message += " *Phone Number"
			valid = false
		}
		
		if (valid == false) {
				alert(message)
				return false
			}
      //this.sendHouseholdEmergencyContactInfoToReviewPage();
      return true;
    }
  },
};
</script>
