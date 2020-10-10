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
				<p>{{dataPersonalInfo.lName}}, {{dataPersonalInfo.fName}} {{dataPersonalInfo.suffix}}</p>
				<p>DOB:  {{dataPersonalInfo.dateOfBirth}}</p>
				<p>Gender ID:  {{dataPersonalInfo.genderID}}</p>
				<p>Race(s):  {{dataPersonalInfo.raceSelections}}</p>
				<p>Ethnicity:  {{dataPersonalInfo.ethnicitySelection}}</p>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<p class="font-weight-medium primary--text">Home Address </p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<p>{{dataHomeAddress.streetAddr}}</p>
				<p>{{dataHomeAddress.cityAddr}}, {{dataHomeAddress.countyAddr}}, {{dataHomeAddress.stateAddr}}, {{dataHomeAddress.countryAddr}}, {{dataHomeAddress.zipAddr}}</p>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<p class="font-weight-medium primary--text"> Contact Information</p>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<p>Phone: {{dataContactInfo.contactPhoneNumber}} ({{dataContactInfo.contactPhoneNumberType}})</p>
				<p>E-mail: {{dataContactInfo.contactEMail}}</p>
				<p>Follow-up approval: {{dataContactInfo.contactApproval}}</p>
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
			dataPersonalInfo:
			{
				lName: '',
				fName: '',
				suffix: '',
				dateOfBirth: '',
				genderID: '',
				recentPhoto: '../assets/blankPicture.png',
				raceSelections: 'N/A',
				ethnicitySelection: 'N/A'
			},
			dataEContact:
			{
				eContactLastName: '',
				eContactFirstName: '',
				eContactPhoneNumber: '',
				eContactPhoneNumberType: ''
			},
			dataHomeAddress:
			{
				streetAddr: '',
				cityAddr: '',
				countyAddr: '',
				stateAddr: '',
				countryAddr: '',
				zipAddr: ''
			},	
			dataContactInfo:
			{
				contactPhoneNumber: '',
				contactPhoneNumberType: '',
				contactEMail: '',
				contactApproval: ''
			}
		}
	},
	methods:
	{
		updatePersonalInfoData(personalInfoPayload) {
			this.dataPersonalInfo = personalInfoPayload
		},
		updateEmergencyContactData(emergencyContactPayload) {
			this.dataEContact = emergencyContactPayload
		},
		updateHomeAddressData(homeAddressPayload) {
			this.dataHomeAddress = homeAddressPayload
		},
		updateContactInfoData(contactInfoPayload) {
			this.dataContactInfo = contactInfoPayload
		}
	},
	mounted() {
		EventBus.$on('DATA_PERSONAL_INFO_PUBLISHED', (personalInfoPayload) => {
			this.updatePersonalInfoData(personalInfoPayload)
		}),
		EventBus.$on('DATA_EMERGENCY_CONTACT_INFO_PUBLISHED', (emergencyContactPayload) => {
			this.updateEmergencyContactData(emergencyContactPayload)
		}),
		EventBus.$on('DATA_ADDRESS_INFO_PUBLISHED', (homeAddressPayload) => {
			this.updateHomeAddressData(homeAddressPayload)
		}),
		EventBus.$on('DATA_CONTACT_INFO_PUBLISHED', (contactInfoPayload) => {
			this.updateContactInfoData(contactInfoPayload)
		})
	}
}
</script>

