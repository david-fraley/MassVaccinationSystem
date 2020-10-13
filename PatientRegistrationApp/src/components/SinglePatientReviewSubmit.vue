<template>
	<v-container fluid>
		<v-row align="center" justify="center">
			<v-heading> Please review all entries before submitting the registration! </v-heading>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6"	md="3">
				<div class="font-weight-medium primary--text"> Personal information</div>
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
				<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataPersonalInfo.lName}}, {{dataPersonalInfo.fName}} {{dataPersonalInfo.suffix}}</span></div>
				<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataPersonalInfo.dateOfBirth}}</span></div>
				<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataPersonalInfo.genderID}}</span></div>
				<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataPersonalInfo.raceSelections}}</span></div>
				<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataPersonalInfo.ethnicitySelection}}</span></div>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text">Home Address </div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Street Address:  <span class="font-weight-regular">{{dataHomeAddress.streetAddr}}</span></div>
				<div class="font-weight-medium">City, County, State, Country, Zip Code:  <span class="font-weight-regular">{{dataHomeAddress.cityAddr}}, {{dataHomeAddress.countyAddr}}, {{dataHomeAddress.stateAddr}}, {{dataHomeAddress.countryAddr}}, {{dataHomeAddress.zipAddr}}</span></div>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text"> Contact Information</div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Phone: <span class="font-weight-regular">{{dataContactInfo.contactPhoneNumber}} ({{dataContactInfo.contactPhoneNumberType}})</span></div>
				<div class="font-weight-medium">E-mail: <span class="font-weight-regular">{{dataContactInfo.contactEMail}}</span></div>
				<div class="font-weight-medium">Follow-up approval: <span class="font-weight-regular">{{dataContactInfo.contactApproval}}</span></div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text"> Emergency Contact </div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataEContact.eContactLastName}}, {{dataEContact.eContactFirstName}}</span></div>
				<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataEContact.eContactPhoneNumber}} ({{dataEContact.eContactPhoneNumberType}})</span></div>
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

