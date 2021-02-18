<template>
  <v-container fluid>
    <v-row align="center" justify="start">
      <v-col cols="12">
        <div class="font-weight-regular">
          Use this QR code to easily check-in at the site where you receive your
          vaccine. This QR code contains an encrypted patient identifier so we
          can quickly and securely identify you and retrieve your information.
        </div>
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
      <div class="font-weight-medium">
        Name:
        <span class="font-weight-regular"
          >{{ dataPersonalInfo.familyName }}, {{ dataPersonalInfo.givenName }}
          {{ dataPersonalInfo.middleName }} {{ dataPersonalInfo.suffix }}</span
        >
      </div>
    </v-row>
    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text" @click="generatePdf">
        Download As PDF
      </v-btn>
    </v-row>
    <v-row justify="center">
      <div class="font-weight-medium">
        <br /><br />How do you want to receive this QR code?
      </div>
    </v-row>
    <v-row justify="center">
      <v-col cols="8" sm="4" md="3" lg="3">
        <v-radio-group v-model="sendQr" class="font-weight-medium">
          <v-radio value="Email" label="E-mail"></v-radio>
          <v-radio value="SmsMessage" label="SMS Message"></v-radio>
          <v-radio value="Both" label="Both"></v-radio>
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
import EventBus from "../eventBus";
import VueQrcode from "vue-qrcode";
import jsPDF from "jspdf";

export default {
  data() {
    return {
      dataPersonalInfo: [],
      correctionLevel: "H",
      qrValue: " ",
      sendQr: " ",
      dataScreeningResponses: [],
    };
  },
  methods: {
    generatePdf() {
      const qrcode = document.getElementById("qrCodeId");
      let pdfDoc = new jsPDF();
      let imageData = this.getBase64Image(qrcode);
      let string =
        this.dataPersonalInfo.familyName +
        ", " +
        this.dataPersonalInfo.givenName +
        " " +
        this.dataPersonalInfo.middleName +
        " " +
        this.dataPersonalInfo.suffix;
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
    updatePersonalInfoData(personalInfoPayload) {
      this.dataPersonalInfo = personalInfoPayload;
    },
    updateQrCodeData(data) {
      this.qrValue = data;
      //temporary - add the screening responses to the qrValue string:
      this.qrValue +=
        "|" +
        this.dataScreeningResponses.screeningQ1 +
        "|" +
        this.dataScreeningResponses.screeningQ2 +
        "|" +
        this.dataScreeningResponses.screeningQ2b +
        "|" +
        this.dataScreeningResponses.screeningQ3a +
        "|" +
        this.dataScreeningResponses.screeningQ3b +
        "|" +
        this.dataScreeningResponses.screeningQ3c +
        "|" +
        this.dataScreeningResponses.screeningQ4 +
        "|" +
        this.dataScreeningResponses.screeningQ5 +
        "|" +
        this.dataScreeningResponses.screeningQ6 +
        "|" +
        this.dataScreeningResponses.screeningQ7 +
        "|" +
        this.dataScreeningResponses.screeningQ8 +
        "|";
      console.log(this.qrValue)
    },
    updateScreeningResponseData(screeningResponsesPayload) {
      this.dataScreeningResponses = screeningResponsesPayload;
    },
  },
  components: {
    VueQrcode,
  },
  mounted() {
    EventBus.$on("DATA_PERSONAL_INFO_PUBLISHED", (personalInfoPayload) => {
      this.updatePersonalInfoData(personalInfoPayload);
    }),
      EventBus.$on(
        "DATA_SCREENING_RESPONSES_PUBLISHED",
        (screeningResponsesPayload) => {
          this.updateScreeningResponseData(screeningResponsesPayload);
        }
      );
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
