<template>
	<v-container fluid><div id="printMe"> 
  <v-row justify="center">
  <div class="no-printme font-weight-medium"><br><br>How do you want to receive this QR code?</div>
</v-row>
<v-row justify="center">
  <v-col cols="8" sm="4" md="3" lg="3">
  <v-radio-group class="no-printme font-weight-medium">
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
    <v-row align="center" justify="start">
      <v-col cols="12">
      <div id="printMe" class="font-weight-regular">Use this QR code to easily check-in at the site where you receive your vaccine. This QR code contains an encrypted patient identifier so we can quickly and securely identify you and retrieve your information.</div></v-col></v-row>
    <v-row justify="center">
      <div>
				<vue-qrcode
          id="printMe"
          v-bind:value="qrValue"
					v-bind:errorCorrectionLevel="correctionLevel" />
			</div></v-row>

     <v-row justify="center">
      <div id="printMe" class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataPersonalInfo.familyName}}, 
					{{dataPersonalInfo.givenName}} {{dataPersonalInfo.middleName}} {{dataPersonalInfo.suffix}}</span></div>
    </v-row>
    </div>

    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text" @click="printDiv('printMe')">
								Download As PDF
			</v-btn>
    </v-row>


	</v-container>
</template>

<script>
import EventBus from '../eventBus';
import VueQrcode from 'vue-qrcode';
//import jsPDF from 'jspdf';

export default {
  data() {
    return {
      dataPersonalInfo: [],
      correctionLevel: "H",
      qrValue: ' '
    };
  },
  methods: {
    printDiv(divName){
      var printContents = document.getElementById(divName).innerHTML;
      var originalContents = document.body.innerHTML;
      document.body.innerHTML = printContents;
      window.print();
      document.body.innerHTML = originalContents;
    },
    /*print() {
      const modal = document.getElementById("print")
      const cloned = modal.cloneNode(true)
      let section = document.getElementById("print")
      if (!section) {
        section = document.createElelment("div")
        document.body.appendChild(section)
      }
      section.innerHTML = "";
      section.appendChild(cloned);
      window.print()
    },*/
   /* generatePdf(){
      const doc = new jsPDF();
      doc.text(this.qrValue,15,15);
      //doc.text("hello world", 15,15);
      doc.save("qrcode.pdf")
    },*/
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

<style lang="css" scoped>
.printme {
	display: none;
}
@media print {
	.no-printme  {
		display: none;
	}
	.printme  {
		display: block;
	}
}
</style>
