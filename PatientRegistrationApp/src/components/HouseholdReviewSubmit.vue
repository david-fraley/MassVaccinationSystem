<template>
	<v-container fluid>
		<v-row align="center" justify="center">
			<v-heading> Please review all entries before submitting the registration! </v-heading>
		</v-row>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text">Household Home Address </div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Street Address:  <span class="font-weight-regular">{{dataHouseholdHomeAddress.householdLineAddress}}</span></div>
				<div class="font-weight-medium">City, County, State, Country, Zip Code:  <span class="font-weight-regular">{{dataHouseholdHomeAddress.householdCityAddress}}, 
					{{dataHouseholdHomeAddress.householdDistrictAddress}}, {{dataHouseholdHomeAddress.householdStateAddress}}, {{dataHouseholdHomeAddress.householdCountryAddress}}, {{dataHouseholdHomeAddress.householdPostalCode}}</span></div>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text">Household Contact Information</div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Primary Phone: <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
					({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
				<div class="font-weight-medium">Secondary Phone: <span class="font-weight-regular">{{dataHouseholdContactInfo.secondaryPhoneNumber}} 
					({{dataHouseholdContactInfo.secondaryPhoneNumberType}})</span></div>
				<div class="font-weight-medium">Primary E-mail: <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryEmail}}</span></div>
				<div class="font-weight-medium">Secondary E-mail: <span class="font-weight-regular">{{dataHouseholdContactInfo.secondaryEmail}}</span></div>
				<div class="font-weight-medium">Follow-up approval: <span class="font-weight-regular">{{dataHouseholdContactInfo.approval}}</span></div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<v-card class="d-flex flex-wrap" flat>
					<v-card class="pa-2" flat min-width=33%>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #1</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdFamilyName}}, 
						{{dataHouseholdPersonalInfo[0].householdGivenName}} {{dataHouseholdPersonalInfo[0].householdSuffix}}</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdBirthDate}}</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdGender}}</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdRaceSelections}}</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdEthnicitySelection}}</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactFamilyName}}, 
						{{dataHouseholdEmergencyContact.householdEmergencyContactGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumber}} 
						({{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumberType}})</span></div>
					</v-card>
			
					<v-card 
						v-for="n in numberOfHouseholdMembers-1"
						:key="n"
						class="pa-2" flat min-width=33%>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #{{n+1}}</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[n].householdFamilyName}}, 
						{{dataHouseholdPersonalInfo[n].householdGivenName}} {{dataHouseholdPersonalInfo[n].householdSuffix}}</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[n].householdBirthDate}}</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[n].householdGender}}</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[n].householdRaceSelections}}</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[n].householdEthnicitySelection}}</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdFamilyName}}, 
						{{dataHouseholdPersonalInfo[0].householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
					</v-card>
				</v-card>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

	export default {
	data () {
		return {
			numberOfHouseholdMembers: 2,
			dataHouseholdPersonalInfo: [
				{householdFamilyName: ''},
				{householdGivenName: ''},
				{householdSuffix: ''},
				{householdBirthDate: ''},
				{householdGender: ''},
				{householdPatientPhoto: ''},
				{householdRaceSelections: 'N/A'},
				{householdEthnicitySelection: 'N/A'},
			],
			dataHouseholdEmergencyContact:
			{
				householdEmergencyContactFamilyName: '',
				householdEmergencyContactGivenName: '',
				householdEmergencyContactPhoneNumber: '',
				householdEmergencyContactPhoneNumberType: ''
			},
			dataHouseholdHomeAddress:
			{
				householdLineAddress: '',
				householdCityAddress: '',
				householdDistrictAddress: '',
				householdStateAddress: '',
				householdCountryAddress: '',
				householdPostalCode: ''
			},	
			dataHouseholdContactInfo:
			{
				primaryPhoneNumber: '',
				primaryPhoneNumberType: '',
				primaryEmail: '',
				secondaryPhoneNumber: '',
				secondaryPhoneNumberType: '',
				secondaryEmail: '',
				approval: ''
			}
		}
	},
	methods:
	{
		setNumberOfHouseholdMembers(householdCountPayload)
		{
			this.numberOfHouseholdMembers = householdCountPayload.householdCount
		},
		updateHomeAddressData(householdHomeAddressPayload) {
			this.dataHouseholdHomeAddress = householdHomeAddressPayload
		},
		updateHouseholdContactInfoData(householdContactInfoPayload) {
			this.dataHouseholdContactInfo = householdContactInfoPayload
		},
		updateHouseholdPersonalInfoData(householdPersonalInfoPayload, householdMemberNumber) {
			this.dataHouseholdPersonalInfo[householdMemberNumber-1] = householdPersonalInfoPayload
		},
		updateHouseholdEmergencyContactData(householdEmergencyContactPayload) {
			this.dataHouseholdEmergencyContact = householdEmergencyContactPayload
		},
	},
	mounted() 
	{
		EventBus.$on('DATA_HOUSEHOLD_COUNT_UPDATED', (householdCountPayload) => {
			this.setNumberOfHouseholdMembers(householdCountPayload)
		}),
		EventBus.$on('DATA_HOUSEHOLD_ADDRESS_INFO_PUBLISHED', (householdHomeAddressPayload) => {
			this.updateHomeAddressData(householdHomeAddressPayload)
		}),
		EventBus.$on('DATA_HOUSEHOLD_CONTACT_INFO_PUBLISHED', (householdContactInfoPayload) => {
			this.updateHouseholdContactInfoData(householdContactInfoPayload)
		}),
		EventBus.$on('DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED', (householdPersonalInfoPayload, householdMemberNumber) => {
			this.updateHouseholdPersonalInfoData(householdPersonalInfoPayload, householdMemberNumber)
		}),
		EventBus.$on('DATA_HOUSEHOLD_EMERGENCY_CONTACT_INFO_PUBLISHED', (householdEmergencyContactPayload) => {
			this.updateHouseholdEmergencyContactData(householdEmergencyContactPayload)
		})
	},
}
</script>
