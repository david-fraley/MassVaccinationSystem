<template>
	<v-container fluid>
		<v-row align="center" justify="start">
      <v-col cols="12">
      <div class="font-weight-regular">Use these QR codes to easily check-in at the site where you receive your vaccine. This QR code contains an encrypted patient identifier so we can quickly and securely identify you and retrieve your information.</div></v-col></v-row>
		<v-row justify="center">
			<v-btn color="secondary" class="ma-2 white--text" @click="generatePdf">
				Download As PDF
			</v-btn>
		</v-row>
		<v-row justify="center">	
			<v-col cols="12">
				<v-card class="d-flex flex-wrap" flat>
				<template v-for="index in getNumberOfHouseholdMembers()">
						<v-card class="pa-2" flat min-width=33%
						:key="index">
							<div class="font-weight-medium primary--text">Household Member #{{index}}</div>
							<v-row><div>
							<vue-qrcode
								v-bind:value="toQRValue(dataHouseholdPersonalInfo[index-1])"
								v-bind:errorCorrectionLevel="correctionLevel" />
							</div></v-row>
							<div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataHouseholdPersonalInfo[index-1].familyName}}, 
							{{dataHouseholdPersonalInfo[index-1].givenName}} {{dataHouseholdPersonalInfo[index-1].middleName}} {{dataHouseholdPersonalInfo[index-1].suffix}}</span></div>
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
	</v-container>
</template>

<script>
import EventBus from '../eventBus';
import VueQrcode from 'vue-qrcode';
import jsPDF from 'jspdf';

	export default {
	data () {
		return {
			dataHouseholdPersonalInfo: [],
			correctionLevel: "H"
		}
	},
	props:
	{
		numberOfHouseholdMembers: Number
	},
	methods:
	{
		generatePdf() {
			let pdfDoc = new jsPDF();
			pdfDoc.setFontSize(10);
			pdfDoc.text("COVID-19 Vaccination Registration",10,15);
			pdfDoc.text("Use this QR code to easily check-in at the site where you receive your vaccine.",10,25);
			pdfDoc.text("This QR code contains an encrypted patient identifier so we can quickly and securely identify you and retrieve your information.",10, 30);
			pdfDoc.save('COVID-19_Registration_QR_Code.pdf');
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
		getNumberOfHouseholdMembers()
		{
			return this.numberOfHouseholdMembers;
		},
		toQRValue(member) {
			const value = member.familyName + ", " + member.givenName + " " + member.middleName + " " + member.suffix;
			return value;
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
