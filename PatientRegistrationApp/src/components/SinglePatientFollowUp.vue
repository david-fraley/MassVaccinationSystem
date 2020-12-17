<template>
	<v-container fluid>
    <v-row>


<v-card>
  <v-card-title prepend-icon="mdi-qrcode">
    QR code generator
  </v-card-title>
  <v-card-text>
    <v-text-field
      v-model="qrText"
      clearable
      counter
      filled
      label="Type some text here"
      @input="generateQrCode"
    ></v-text-field>
</v-card-text>



  <v-card-actions>
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
    </v-card-actions>
    </v-card>
    </v-row>
	</v-container>
</template>

<script>
import qrCode from 'qrcode'

export default {
  name: "SinglePatientFollowUp",
  data() {
    return {
      qrText: '',
      qrSrc: null
      
    };
  },
beforeDestroy (){
  this.reset()
},
  methods: {
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
