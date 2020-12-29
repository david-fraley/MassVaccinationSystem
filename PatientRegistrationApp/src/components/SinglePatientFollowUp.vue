<template>
	<div ref="content"><v-container fluid> 
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
    <v-row>
      <v-btn 
      color="accent"
      @click="generatePdf"
    >
    Download as PDF 
    </v-btn></v-row>
	</v-container></div>
</template>

<script>
import qrCode from 'qrcode';
import jsPDF from 'jspdf';


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
    generatePdf(){
      const doc = new jsPDF();
      doc.text("hello world", 15,15);
      doc.save("pdf.pdf");
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
