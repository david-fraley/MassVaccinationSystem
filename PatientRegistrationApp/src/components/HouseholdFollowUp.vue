<template>
	<v-container fluid>
		<v-row align="center" justify="center">
		</v-row>
		<v-row>
			<v-col cols="12" sm="6"	md="3">
				<div class="font-weight-medium primary--text"> QR Code Page</div>
			</v-col>
		</v-row>

		<div>
			<vue-qrcode
			v-bind:value="qrValue"
			v-bind:errorCorrectionLevel="correctionLevel" />
		</div>
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
methods:
	{
	updatePersonalInfoData(personalInfoPayload) {
		this.dataPersonalInfo = personalInfoPayload
		//this.qrValue= personalInfoPayload.familyName
	}
},
components:{
	VueQrcode
},
mounted() {
		EventBus.$on('DATA_PERSONAL_INFO_PUBLISHED', (personalInfoPayload) => {
			this.updatePersonalInfoData(personalInfoPayload)
    })
}
}

</script>