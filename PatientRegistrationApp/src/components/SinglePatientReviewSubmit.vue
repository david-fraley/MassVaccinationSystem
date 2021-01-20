<template>
	<v-container fluid>
		<v-row align="center" justify="center">
		</v-row>
		<v-row>
			<v-col cols="12" sm="6"	md="3">
				<div class="font-weight-medium primary--text"> Personal information</div>
			</v-col>
		</v-row>
		<v-row>
			<v-col style="flex-grow:0" v-bind:class="{ hidden: dataPersonalInfo ? !dataPersonalInfo.patientPhotoSrc : true }">
				<v-img
					style="float:left"
					contain		
					max-height="300"
					max-width="300" 
					:src="dataPersonalInfo.patientPhotoSrc">
				</v-img>
			</v-col>  
			<v-col cols="8" sm="6">
				<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataPersonalInfo.familyName}}, 
					{{dataPersonalInfo.givenName}} {{dataPersonalInfo.middleName}} {{dataPersonalInfo.suffix}}</span></div>
				<div class="font-weight-medium">Date of Birth:  <span class="font-weight-regular">{{dataPersonalInfo.birthDate}}</span></div>
				<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataPersonalInfo.gender}}</span></div>
				<div class="font-weight-medium">Race:  <span class="font-weight-regular">{{dataPersonalInfo.race}}</span></div>
				<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataPersonalInfo.ethnicity}}</span></div>
				<div class="font-weight-medium">Preferred Language:  <span class="font-weight-regular">{{dataPersonalInfo.preferredLanguage}}</span></div>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text">Address</div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Address Type:  <span class="font-weight-regular">{{dataHomeAddress.addressType}}</span></div>
				<div class="font-weight-regular">{{dataHomeAddress.lineAddress1}}</div>
				<template v-if="dataHomeAddress.lineAddress2 != ''">
					<div class="font-weight-regular">{{dataHomeAddress.lineAddress2}}</div>
				</template>
				<div class="font-weight-regular">{{dataHomeAddress.cityAddress}}, {{dataHomeAddress.stateAddress}}, 
					{{dataHomeAddress.countryAddress}}, {{dataHomeAddress.postalCode}}</div>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text"> Contact Information</div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<template v-if="dataContactInfo.patientPhoneNumber != ''">
					<div class="font-weight-medium">Phone: <span class="font-weight-regular">{{dataContactInfo.patientPhoneNumber}} 
						<span v-if="dataContactInfo.patientPhoneNumberType != ''">({{dataContactInfo.patientPhoneNumberType}})</span></span></div>
				</template>
				<template v-else>
					<div class="font-weight-medium">Phone: <span class="font-weight-regular">Not provided</span></div>
				</template>
				<template v-if="dataContactInfo.patientEmail != ''">
					<div class="font-weight-medium">E-mail: <span class="font-weight-regular">{{dataContactInfo.patientEmail}}</span></div>
				</template>
				<template v-else>
					<div class="font-weight-medium">E-mail: <span class="font-weight-regular">Not provided</span></div>
				</template>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text"> Emergency Contact </div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataEmergencyContact.emergencyContactFamilyName}}, 
					{{dataEmergencyContact.emergencyContactGivenName}}</span></div>
				<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataEmergencyContact.emergencyContactPhoneNumber}} 
					<span v-if="dataEmergencyContact.emergencyContactPhoneNumberType != ''">({{dataEmergencyContact.emergencyContactPhoneNumberType}})</span></span></div>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12">
			<p> </p>
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
				familyName: '',
				givenName: '',
				suffix: '',
				birthDate: '',
				gender: '',
				patientPhoto: '../assets/blankPicture.png',
				race: 'N/A',
				ethnicity: 'N/A'
			},
			dataEmergencyContact:
			{
				emergencyContactFamilyName: '',
				emergencyContactFivenName: '',
				emergencyContactPhoneNumber: '',
				emergencyContactPhoneNumberType: ''
			},
			dataHomeAddress:
			{
				lineAddress: '',
				cityAddress: '',
				districtAddress: '',
				stateAddress: '',
				countryAddress: '',
				postalCode: ''
			},	
			dataContactInfo:
			{
				patientPhoneNumber: '',
				patientPhoneNumberType: '',
				patientEmail: '',
				approval: ''
			}
		}
	},
	methods:
	{
		updatePersonalInfoData(personalInfoPayload) {
			this.dataPersonalInfo = personalInfoPayload
		},
		updateEmergencyContactData(emergencyContactPayload) {
			this.dataEmergencyContact = emergencyContactPayload
		},
		updateHomeAddressData(homeAddressPayload) {
			this.dataHomeAddress = homeAddressPayload
		},
		updateContactInfoData(contactInfoPayload) {
			this.dataContactInfo = contactInfoPayload
		},	
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
<style lang="css" scoped>

	.hidden {
		display: none;
	}

</style>
