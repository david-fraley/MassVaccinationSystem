<template>
	<v-container fluid>
		<v-row align="center" justify="center">
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
						<v-img 
							contain		
							max-height="300"
							max-width="300" 
							:src="dataHouseholdPersonalInfo[0] ? dataHouseholdPersonalInfo[0].householdPatientPhotoSrc : undefined"
							v-bind:class="{ hidden: 
									dataHouseholdPersonalInfo[0] ? !dataHouseholdPersonalInfo[0].householdPatientPhotoSrc : true }">
						</v-img>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #1</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdFamilyName}}, 
						{{dataHouseholdPersonalInfo[0].householdGivenName}} {{dataHouseholdPersonalInfo[0].householdSuffix}}</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdBirthDate}}</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdGender}}</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdRaceSelections}}</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdEthnicitySelection}}</span></div>
						<div class="font-weight-medium">Preferred Language:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].preferredLanguage}}</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactFamilyName}}, 
						{{dataHouseholdEmergencyContact.householdEmergencyContactGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumber}} 
						({{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumberType}})</span></div>
						<v-card-actions>
							<v-btn outlined small color="primary" @click="editPersonalInfo(1)">
								Edit
							</v-btn>
						</v-card-actions>
					</v-card>

					<template v-for="index in getNumberOfHouseholdMembers()-1">
						<v-card class="pa-2" flat min-width=33%
						:key="index">
							<v-img
								contain		
								max-height="300"
								max-width="300" 
								:src="dataHouseholdPersonalInfo[index] ? dataHouseholdPersonalInfo[index].householdPatientPhotoSrc : undefined"
								v-bind:class="{ hidden: 
									dataHouseholdPersonalInfo[index] ? !dataHouseholdPersonalInfo[index].householdPatientPhotoSrc : true }">
							</v-img>
							<div class="font-weight-medium primary--text">Personal Info: Household Member #{{index+1}}</div>
							<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].householdFamilyName}}, 
							{{dataHouseholdPersonalInfo[index].householdGivenName}} {{dataHouseholdPersonalInfo[index].householdSuffix}}</span></div>
							<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].householdBirthDate}}</span></div>
							<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].householdGender}}</span></div>
							<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].householdRaceSelections}}</span></div>
							<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].householdEthnicitySelection}}</span></div>
							<div class="font-weight-medium">Preferred Language:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].preferredLanguage}}</span></div>
							<div class="font-weight-light primary--text">Emergency Contact</div>
							<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].householdFamilyName}}, 
							{{dataHouseholdPersonalInfo[0].householdGivenName}}</span></div>
							<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
							({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
							<v-card-actions>
							<v-btn outlined small color="primary" @click="editPersonalInfo(index+1)">
								Edit
							</v-btn>
						</v-card-actions>
						</v-card>
					</template>
				</v-card>
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
			dataHouseholdPersonalInfo: [],
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
	props:
	{
		numberOfHouseholdMembers: Number
	},
	methods:
	{
		updateHomeAddressData(householdHomeAddressPayload) {
			this.dataHouseholdHomeAddress = householdHomeAddressPayload
		},
		updateHouseholdContactInfoData(householdContactInfoPayload) {
			this.dataHouseholdContactInfo = householdContactInfoPayload
		},
		updateHouseholdPersonalInfoData(householdPersonalInfoPayload, householdMemberNumber) {
			// if member is next in line, add to array of members
			if (householdMemberNumber-1 == this.dataHouseholdPersonalInfo.length) {
				this.dataHouseholdPersonalInfo.push(householdPersonalInfoPayload);
			// else if member already exists in array, update the member
			} else if (householdMemberNumber-1 < this.dataHouseholdPersonalInfo.length) {
				this.$set(this.dataHouseholdPersonalInfo, householdMemberNumber-1, householdPersonalInfoPayload);
			}
		},
		updateHouseholdEmergencyContactData(householdEmergencyContactPayload) {
			this.dataHouseholdEmergencyContact = householdEmergencyContactPayload
		},
		getNumberOfHouseholdMembers()
		{
			return this.numberOfHouseholdMembers;
		},
		editPersonalInfo(householdMemberNumber)
		{
			EventBus.$emit('DATA_HOUSEHOLD_PERSONAL_INFO_EDIT', householdMemberNumber)
		}
	},
	mounted() 
	{
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
<style lang="css" scoped>

	.hidden {
		display: none;
	}

</style>
