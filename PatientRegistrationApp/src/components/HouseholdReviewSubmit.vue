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
				<div class="font-weight-medium">Street Address:  <span class="font-weight-regular">Line Address</span></div>
				<div class="font-weight-medium">City, County, State, Country, Zip Code:  <span class="font-weight-regular">City Address, 
					District, State, Country, Postal Code</span></div>
			</v-col>
		</v-row>

		<v-row>
			<v-col cols="12" sm="6" md="3">
				<div class="font-weight-medium primary--text">Household Contact Information</div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="12">
				<div class="font-weight-medium">Primary Phone: <span class="font-weight-regular">Phone number 
					(Phone Type)</span></div>
				<div class="font-weight-medium">Secondary Phone: <span class="font-weight-regular">Phone number 
					(Phone Type)</span></div>
				<div class="font-weight-medium">Primary E-mail: <span class="font-weight-regular">e-mail</span></div>
				<div class="font-weight-medium">Secondary E-mail: <span class="font-weight-regular">e-mail</span></div>
				<div class="font-weight-medium">Follow-up approval: <span class="font-weight-regular">yes/no</span></div>
			</v-col>
		</v-row>

		<v-row>	
			<v-col cols="6">
				<v-card class="pa-2" outlined tile>
					<div class="font-weight-medium primary--text">Personal Info: Household Member #1</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
					Given name Suffix</span></div>
					<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
					<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
					<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
					<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
					<div class="font-weight-light primary--text">Emergency Contact</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
					Given name</span></div>
					<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
					(phone type)</span></div>
				</v-card>
			</v-col>
			<v-col cols="6">
				<v-card class="pa-2" outlined tile>
					<div class="font-weight-medium primary--text">Personal Info: Household Member #2</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
					Given name Suffix</span></div>
					<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
					<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
					<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
					<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
					<div class="font-weight-light primary--text">Emergency Contact</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
					Given name</span></div>
					<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
					(phone type)</span></div>
				</v-card>
			</v-col>
			<template v-if="numberOfHouseholdMembers >=3">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #3</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=4">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #4</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>	
			</template>
			<template v-if="numberOfHouseholdMembers >=5">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #5</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=6">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #6</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>	
			</template>
			<template v-if="numberOfHouseholdMembers >=7">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #7</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=8">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #8</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>	
			</template>
			<template v-if="numberOfHouseholdMembers >=9">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #9</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=10">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #10</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=11">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #11</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=12">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #12</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=13">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #13</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=14">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #14</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>	
			</template>
			<template v-if="numberOfHouseholdMembers >=15">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #15</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=16">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #16</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=17">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #17</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=18">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #18</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>	
			</template>
			<template v-if="numberOfHouseholdMembers >=19">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #19</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
			<template v-if="numberOfHouseholdMembers >=20">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #20</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gener ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">Phone number 
						(phone type)</span></div>
					</v-card>
				</v-col>
			</template>
		</v-row>


	</v-container>
</template>

<script>
import EventBus from '../eventBus'

	export default {
	data () {
		return {
			numberOfHouseholdMembers: 2
			/*dataPersonalInfo:
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
			}*/
		}
	},
	methods:
	{
		setNumberOfHouseholdMembers(householdCountPayload)
		{
			this.numberOfHouseholdMembers = householdCountPayload.householdCount
		},
	},
	mounted() 
	{
		EventBus.$on('DATA_HOUSHOLD_COUNT_UPDATED', (householdCountPayload) => {
			this.setNumberOfHouseholdMembers(householdCountPayload)
		})
	},
}
</script>
