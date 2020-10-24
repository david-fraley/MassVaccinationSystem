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
			<v-col cols="6">
				<v-card class="pa-2" outlined tile>
					<div class="font-weight-medium primary--text">Personal Info: Household Member #1</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
					{{dataHouseholdPersonalInfo.householdGivenName}} {{dataHouseholdPersonalInfo.householdSuffix}}</span></div>
					<div class="font-weight-medium">DOB:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdBirthDate}}</span></div>
					<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdGender}}</span></div>
					<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdRaceSelections}}</span></div>
					<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdEthnicitySelection}}</span></div>
					<div class="font-weight-light primary--text">Emergency Contact</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactFamilyName}}, 
					{{dataHouseholdEmergencyContact.householdEmergencyContactGivenName}}</span></div>
					<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumber}} 
					({{dataHouseholdEmergencyContact.householdEmergencyContactPhoneNumberType}})</span></div>
				</v-card>
			</v-col>
			<v-col cols="6">
				<v-card class="pa-2" outlined tile>
					<div class="font-weight-medium primary--text">Personal Info: Household Member #2</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
					Given name Suffix</span></div>
					<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
					<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
					<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
					<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
					<div class="font-weight-light primary--text">Emergency Contact</div>
					<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
					{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
					<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
					({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
				</v-card>
			</v-col>
			<template v-if="numberOfHouseholdMembers >=3">
				<v-col cols="6">
					<v-card class="pa-2" outlined tile>
						<div class="font-weight-medium primary--text">Personal Info: Household Member #3</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">Family name, 
						Given name Suffix</span></div>
						<div class="font-weight-medium">DOB:  <span class="font-weight-regular">Birth date</span></div>
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
						<div class="font-weight-medium">Gender ID:  <span class="font-weight-regular">Gender ID</span></div>
						<div class="font-weight-medium">Race(s):  <span class="font-weight-regular">Race(s)</span></div>
						<div class="font-weight-medium">Ethnicity:  <span class="font-weight-regular">Ethnicity</span></div>
						<div class="font-weight-light primary--text">Emergency Contact</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo.householdFamilyName}}, 
						{{dataHouseholdPersonalInfo.householdGivenName}}</span></div>
						<div class="font-weight-medium">Phone:  <span class="font-weight-regular">{{dataHouseholdContactInfo.primaryPhoneNumber}} 
						({{dataHouseholdContactInfo.primaryPhoneNumberType}})</span></div>
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
			numberOfHouseholdMembers: 2,
			dataHouseholdPersonalInfo:
			{
				householdFamilyName: '',
				householdGivenName: '',
				householdSuffix: '',
				householdBirthDate: '',
				householdGender: '',
				householdPatientPhoto: '',
				householdRaceSelections: 'N/A',
				householdEthnicitySelection: 'N/A',
			},
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
			this.dataHouseholdPersonalInfo = householdPersonalInfoPayload
			alert(householdMemberNumber)
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
