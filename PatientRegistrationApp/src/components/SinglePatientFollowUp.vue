<template>
	<v-container fluid>

    <v-row align="center" justify="start">
      <v-col cols="12">
      <div class="font-weight-regular">Use this QR code to easily check-in at the site where you receive your vaccine. This QR code contains an encrypted patient identifier so we can quickly and securely identify you and retrieve your information.</div></v-col></v-row> 
    <v-row justify="center">
      <div>
				<vue-qrcode
					v-bind:value="qrValue"
					v-bind:errorCorrectionLevel="correctionLevel" />
			</div></v-row>

     <v-row justify="center">
      <div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataPersonalInfo.familyName}}, 
					{{dataPersonalInfo.givenName}} {{dataPersonalInfo.middleName}} {{dataPersonalInfo.suffix}}</span></div>
    </v-row>


    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text">
								Download
			</v-btn>
    </v-row>

<v-row justify="center">
  <div class="font-weight-medium"><br><br>How do you want to receive this QR code?</div>
</v-row>
<v-row justify="center">
  <v-col cols="8" sm="4" md="3" lg="3">
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

export default {
  data() {
    return {
      dataPersonalInfo: [],
      correctionLevel: "H",
      qrValue: ' '
    };
  },
  methods: {
    updatePersonalInfoData(personalInfoPayload) {
      this.dataPersonalInfo = personalInfoPayload;
      this.qrValue = this.dataPersonalInfo.familyName + ", " + this.dataPersonalInfo.givenName + " " + this.dataPersonalInfo.middleName + " " + this.dataPersonalInfo.suffix
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


<style lang="sass" scoped>
.accent--border
  border: 1px solid var(--v-accent-base)

.image-preview
  display: block
  max-width: 100%
</style>
