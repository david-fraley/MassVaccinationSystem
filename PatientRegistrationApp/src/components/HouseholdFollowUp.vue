<template>
	<v-container fluid>
		<v-row align="center" justify="center">
		</v-row>
		<v-row>	
			<v-col cols="12">
				<v-card class="d-flex flex-wrap" flat>
					<v-card class="pa-2" flat min-width=33%>
						<div class="font-weight-medium primary--text">Household Member #1</div>
						<div> QR Code Placeholder</div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].familyName}}, 
						{{dataHouseholdPersonalInfo[0].givenName}} {{dataHouseholdPersonalInfo[0].middleName}} {{dataHouseholdPersonalInfo[0].suffix}}</span></div>
						<v-card-actions>
							<v-btn outlined small color="primary">
								Download
							</v-btn>
						</v-card-actions>
					</v-card>
					
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
			dataHouseholdPersonalInfo: []
		}
	},
	props:
	{
		numberOfHouseholdMembers: Number
	},
	methods:
	{
		updateHouseholdPersonalInfoData(householdPersonalInfoPayload, householdMemberNumber) {
			// if member is next in line, add to array of members
			if (householdMemberNumber-1 == this.dataHouseholdPersonalInfo.length) {
				this.dataHouseholdPersonalInfo.push(householdPersonalInfoPayload);
			// else if member already exists in array, update the member
			} else if (householdMemberNumber-1 < this.dataHouseholdPersonalInfo.length) {
				this.$set(this.dataHouseholdPersonalInfo, householdMemberNumber-1, householdPersonalInfoPayload);
			}
		},
		getNumberOfHouseholdMembers()
		{
			return this.numberOfHouseholdMembers;
		},
	},
	mounted() 
	{
		EventBus.$on('DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED', (householdPersonalInfoPayload, householdMemberNumber) => {
			this.updateHouseholdPersonalInfoData(householdPersonalInfoPayload, householdMemberNumber)
		})
	},
}
</script>
<style lang="css" scoped>

	.hidden {
		display: none;
	}

</style>