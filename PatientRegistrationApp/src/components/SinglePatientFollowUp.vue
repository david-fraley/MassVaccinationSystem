<template>
	<v-container fluid>


    <v-row justify="center">
    <v-col class="d-flex" cols="6" sm="6">
  <v-text-field
      v-model="qrText"
      clearable
      counter
      filled
      label="Type some text here"
      @input="generateQrCode"
    ></v-text-field>
    </v-col></v-row>

    <v-row justify="center">
<div class="justify-center" v-if="qrSrc">
  <img class="preview" :src="qrSrc"> </div>
    </v-row>

    <v-row>
    <v-btn
      color="accent"
      :disabled="!qrSrc"
      @click="reset"
    >
    Reset
    </v-btn>
    <v-spacer></v-spacer>
    <v-btn
    color="primary"
      :disabled="!qrSrc"
      @click="openInNewWindow"
    >
    Open 
    </v-btn>
    </v-row>

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
import EventBus from '../eventBus'
import qrCode from 'qrcode'

export default {
  name: "SinglePatientFollowUp",
  data() {
    return {
      dataPersonalInfo:
			{
				familyName: '',
				givenName: '',
        suffix: ''
      },
      qrText: '',
      qrSrc: null
    };
  },
  beforeDestroy (){
  this.reset()
  },
  methods: {
    updatePersonalInfoData(personalInfoPayload) {
      this.dataPersonalInfo = personalInfoPayload
    },
    createObjectUrl (err, canvas) {
      if (!err) {
        canvas.toBlob((blob) => {
          this.qrSrc = window.URL.createObjectURL(blob)
        })
      } else {
        console.warn('generateQrCode:ERROR', err)
      }
    },
    generateQrCode () {
      if (!this.qrText) { return }

      window.URL.revokeObjectURL(this.qrSrc)
      qrCode.toCanvas(this.qrText, {}, this.createObjectUrl)
    },
    openInNewWindow () {
      window.open(this.qrSrc)
    },
    reset () {
      window.URL.revokeObjectURL(this.qrSrc)
      this.qrSrc = null
      this.qrText = ''
    }
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
