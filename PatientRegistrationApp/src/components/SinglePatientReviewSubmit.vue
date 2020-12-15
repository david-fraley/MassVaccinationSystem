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
				<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataPersonalInfo.birthDate}}</span></div>
				<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataPersonalInfo.gender}}</span></div>
				<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataPersonalInfo.raceSelections}}</span></div>
				<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataPersonalInfo.ethnicitySelection}}</span></div>
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
				<div class="font-weight-medium">Phone: <span class="font-weight-regular">{{dataContactInfo.patientPhoneNumber}} 
					({{dataContactInfo.patientPhoneNumberType}})</span></div>
				<div class="font-weight-medium">E-mail: <span class="font-weight-regular">{{dataContactInfo.patientEmail}}</span></div>
				<div class="font-weight-medium">Follow-up approval: <span class="font-weight-regular">{{dataContactInfo.approval}}</span></div>
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
					({{dataEmergencyContact.emergencyContactPhoneNumberType}})</span></div>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12">
			<p> </p>
			</v-col>
		</v-row>
		<v-btn @click="sendRequest" class="btn btn-outline-success my-4">
		Send Request</v-btn>
		<p class="lead">{{response}}</p>
	</v-container>
</template>
 
<script>
import axios from 'axios'
const client = axios.create({
	baseURL: 'http://hapi.fhir.org/baseR4',
	json: true
})
import EventBus from '../eventBus'

	export default {
	data () {
		return {
			response: '',
			dataPersonalInfo:
			{
				familyName: '',
				givenName: '',
				suffix: '',
				birthDate: '',
				gender: '',
				patientPhoto: '../assets/blankPicture.png',
				raceSelections: 'N/A',
				ethnicitySelection: 'N/A'
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

		sendRequest(){
			client({
				method: 'get',
				url: '/Patient/1709156',
			}).then((res) =>{
				this.response = res.data
			}).catch((error) => {
				this.response = error
			})
		},
		sendRequest2() {
			client({
				method:'post',
				url: '/Patient',
				data: {
					resourceType: 'Patient',
					active: 'true',
					name:[
						{
							use: 'official',
							family: 'Chalmers',
							given:[
							'Peter',
							'James'
							]
						}
					],
					gender: 'male',
					birthDate: '1974-12-25'
				}
			}).then((res) => {
				this.response2 = res.data
			}).catch((error) => {
				this.response2 = error
			})
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
<style lang="css" scoped>

	.hidden {
		display: none;
	}

</style>
