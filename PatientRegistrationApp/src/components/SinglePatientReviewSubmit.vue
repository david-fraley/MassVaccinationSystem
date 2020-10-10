<template>
	<v-container fluid>
		<v-row align="center" justify="center">
			<v-heading> Please review all entries before submitting the registration! </v-heading>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6"	md="3">
				<p class="font-weight-medium primary--text"> Personal information</p>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="4" sm="3">
				<v-img
					style="float:left"		
					max-height="100"
					max-width="100" 
					src="../assets/blankPicture.png">
				</v-img>
			</v-col>  
			<v-col cols="8" sm="6">
				<p>{{data.lName}}, {{data.fName}} {{data.suffix}}</p>
				<p>DOB:  {{data.dateOfBirth}}</p>
				<p>Gender ID:  {{data.genderID}}</p>
				<p>Race:  {{data.raceSelections}}</p>
				<p>Ethnicity:  {{data.ethnicitySelection}}</p>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<p class="font-weight-medium primary--text">Home Address </p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<p>1234 Right Lane</p>
				<p>Hilltop, Waukesha County, WI, USA, 53444</p>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<p class="font-weight-medium primary--text"> Contact Information</p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<p>Phone: </p>
				<p>E-mail: </p>
				<p>Follow-up approval: </p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12" sm="6" md="3">
				<p class="font-weight-medium primary--text"> Emergency Contact </p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<p>{{dataEContact.eContactLastName}}, {{dataEContact.eContactFirstName}}</p>
				<p>Phone:  {{dataEContact.eContactPhoneNumber}} ({{dataEContact.eContactPhoneNumberType}})</p>
			</v-col>
		</v-row>
	</v-container>
</template>
 
<script>
import EventBus from '../eventBus'

	export default {
	data () {
		return {
			data:
			{
				lName: '',
				fName: '',
				suffix: '',
				dateOfBirth: '',
				genderID: '',
				raceSelections: 'N/A',
				ethnicitySelection: 'N/A'
			},
			dataEContact:
			{
				eContactLastName: '',
				eContactFirstName: '',
				eContactPhoneNumber: '',
				eContactPhoneNumberType: ''
			}	
		}
	},
	methods:
	{
		updatePersonalInfoData(personalInfoPayload) {
			this.data = personalInfoPayload
		},
		updateEmergencyContactData(emergencyContactPayload) {
			this.dataEContact = emergencyContactPayload
		}
	},
	mounted() {
		EventBus.$on('DATA_PERSONAL_INFO_PUBLISHED', (personalInfoPayload) => {
			this.updatePersonalInfoData(personalInfoPayload)
		}),
		EventBus.$on('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', (emergencyContactPayload) => {
			this.updateEmergencyContactData(emergencyContactPayload)
		})
	}
}
</script>

