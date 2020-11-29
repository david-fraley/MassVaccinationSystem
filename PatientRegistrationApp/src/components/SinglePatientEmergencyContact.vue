<template>
  <v-container fluid>
    <v-row>
      <!-- Last name -->
      <v-col class="d-flex" cols="6" sm="6">
        <v-text-field
			required
			:rules="[v => !!v || 'Last name field is required']"
			v-model="emergencyContactFamilyName"
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
			v-model="emergencyContactGivenName">
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
		required
          :rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
          placeholder="(###)###-####"
          v-mask="'(###)###-####'"
          v-model="emergencyContactPhoneNumber"
          prepend-icon="mdi-menu-right">
			<template #label>
			<span class="red--text"><strong>* </strong></span>Phone Number
			</template>
        </v-text-field>
		</v-col>

      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="6" sm="3">
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
import EventBus from '../eventBus'

export default {
  name: "SinglePatientEmergencyContact",
  data() {
    return {
      phoneType: ["Cell", "Home"],
      emergencyContactFamilyName: '',
      emergencyContactGivenName: '',
      emergencyContactPhoneNumber: '',
      emergencyContactPhoneNumberType: ''
    };
  },
  methods: {
    sendEmergencyContactInfoToReviewPage()
    {
      const emergencyContactPayload = {
        emergencyContactFamilyName: this.emergencyContactFamilyName,
        emergencyContactGivenName: this.emergencyContactGivenName,
        emergencyContactPhoneNumber: this.emergencyContactPhoneNumber,
        emergencyContactPhoneNumberType: this.emergencyContactPhoneNumberType
      }
      EventBus.$emit('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', emergencyContactPayload)
    },
    verifyFormContents()
    {
		//add logic to check form contents
		var valid = true
		var message = "Woops! You need to enter the following field(s):"
		
		if(this.emergencyContactFamilyName == "")  
		{
			message += " Last Name"
			valid = false
		}
		
		if(this.emergencyContactGivenName == "") 
		{
		if(!valid)
				{
				message +=","
				}
			message += " First Name"
			valid = false
		}
		
		
		if (this.emergencyContactPhoneNumber == "") 
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
	this.sendEmergencyContactInfoToReviewPage();
	return true;
    }
},
};
</script>


