<template>
	<v-container fluid>
		<v-row align="center" justify="center">
		</v-row>
		<v-row justify="center">
			<v-btn color="secondary" class="ma-2 white--text">
				Download All
			</v-btn>
		</v-row>
		<v-row justify="center">	
			<v-col cols="12">
				<v-card class="d-flex flex-wrap" flat>
					<v-card class="pa-2" flat min-width=33%>
						<div class="font-weight-medium primary--text">Household Member #1</div>
						<div> QR Code Placeholder </div>
						<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[0].familyName}}, 
						{{dataHouseholdPersonalInfo[0].givenName}} {{dataHouseholdPersonalInfo[0].middleName}} {{dataHouseholdPersonalInfo[0].suffix}}</span></div>
					</v-card>
				<template v-for="index in getNumberOfHouseholdMembers()-1">
						<v-card class="pa-2" flat min-width=33%
						:key="index">
							<div class="font-weight-medium primary--text">Household Member #{{index+1}}</div>
							<div> QR code Placeholder</div>
							<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index].familyName}}, 
							{{dataHouseholdPersonalInfo[index].givenName}} {{dataHouseholdPersonalInfo[index].middleName}} {{dataHouseholdPersonalInfo[index].suffix}}</span></div>
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
		<v-row justify="center">
			<div class="font-weight-medium"><br><br>How do you want to receive these QR codes?</div>
		</v-row>
		<v-row justify="center">
		<v-col cols="3" sm="3" md="3">
			<v-radio-group class="font-weight-medium">
				<v-radio label="E-mail"></v-radio>
				<v-radio label="SMS Message"></v-radio>
				<v-radio label="Both"></v-radio>
			</v-radio-group>
		</v-col>
		</v-row>

		<v-row justify="center">
			<v-btn color="secondary" class="ma-2 white--text">
				Send
			</v-btn>
		</v-row>
		<v-row>
			<div>
			<vue-qrcode
				v-bind:value="qrValue"
				v-bind:errorCorrectionLevel="correctionLevel" />
			</div></v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus';
import VueQrcode from 'vue-qrcode';
export default {
data () {
	return {
	dataPersonalInfo:
	{
		familyName: '',
		givenName: '',
		suffix: ''
	},
	qrValue : "Shahd",
	correctionLevel: "H"		
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
		}
	},
	components:{
		VueQrcode
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
