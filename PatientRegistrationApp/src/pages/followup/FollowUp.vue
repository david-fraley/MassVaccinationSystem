<template>
  <v-container fluid>
    <v-row align="center" justify="start"
            ><v-col cols="12">
              <v-alert
                elevation="2"
                color="info"
                outlined
                :value="showLastStep"
                transition="scroll-y-transition"
              >
                <h2>{{ lastStep }}</h2>
                <div><br />{{ QRcodeStatement }}</div>
                <div><br />{{ ReminderStatement }}</div>
                <div><br />{{ finalStatement }}</div>
              </v-alert>
            </v-col>
          </v-row>
    <v-row justify="center">
      <div>
        <vue-qrcode
          id="qrCodeId"
          v-bind:value="qrValue"
          v-bind:errorCorrectionLevel="correctionLevel"
        />
      </div>
    </v-row>
    <v-row justify="center">
      <div class="font-weight-medium ma-2">
        Name:
        <span class="font-weight-regular"
          >{{ patient.family }}, {{ patient.given }} {{ patient.middle }}
          {{ patient.suffix }}</span
        >
      </div>
    </v-row>
        <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text" @click="generatePdf">
        Download As PDF
      </v-btn>
    </v-row>
    <v-row justify="center">
      <v-btn color="primary" class="ma-2 white--text" @click="goToTimeTap">
        Schedule Your Appointment
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>
import VueQrcode from "vue-qrcode";
import jsPDF from "jspdf";
import customerSettings from "@/customerSettings";

export default {
  name: "SinglePatientFollowUp",
  data() {
    return {
      dataPersonalInfo: [],
      correctionLevel: "H",
      sendQr: " ",
      dataScreeningResponses: [],
      qrValue: this.$store.state.patient.patient.identifier,
      lastStep: customerSettings.lastStep,
      QRcodeStatement: customerSettings.QRcodeStatement,
      ReminderStatement: customerSettings.ReminderStatement,
      finalStatement: customerSettings.finalStatement,
    };
  },
  computed: {
    patient() {
      return this.$store.state.patient.patient;
    },
  },
  methods: {
    generatePdf() {
      const qrcode = document.getElementById("qrCodeId");
      let pdfDoc = new jsPDF();
      let imageData = this.getBase64Image(qrcode);
      let string =
        this.patient.family +
        ", " +
        this.patient.given +
        " " +
        this.patient.middle +
        " " +
        this.patient.suffix;
      pdfDoc.setFontSize(10);
      pdfDoc.text("COVID-19 Vaccination Registration", 10, 15);
      pdfDoc.text(
        "Use this QR code to easily check-in at the site where you receive your vaccine.",
        10,
        25
      );
      pdfDoc.text(
        "This QR code contains an encrypted patient identifier so we can quickly and securely identify you and retrieve your information.",
        10,
        30
      );
      pdfDoc.addImage(imageData, "JPG", 70, 50);
      pdfDoc.text(string, 75, 48);
      pdfDoc.save("COVID-19_Registration_QR_Code.pdf");
    },
    getBase64Image(img) {
      var canvas = document.createElement("canvas");
      canvas.width = 400;
      canvas.height = 400;
      var ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0);
      var dataURL = canvas.toDataURL("image/png");
      return dataURL;
    },
    goToTimeTap(){
      window.open('https://google.com','_self');
    }
  },
  components: {
    VueQrcode,
  },
  mounted() {
    if (!this.patient.identifier) {
      this.$router.push("/");
    }
  },
};
</script>

<style lang="sass" scoped>
.accent--border
  border: 1px solid var(--v-accent-base)

.image-preview
  display: block
  max-width: 100%
</style>
