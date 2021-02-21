<template>
  <v-container fluid>
    <v-row align="center" justify="start">
      <v-col cols="12">
        <div class="font-weight-regular">
          Use these QR codes to easily check-in at the site where you receive
          your vaccine. This QR code contains an encrypted patient identifier so
          we can quickly and securely identify you and retrieve your
          information.
        </div></v-col
      ></v-row
    >
    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text" @click="generatePdf">
        Download As PDF
      </v-btn>
    </v-row>
    <v-row justify="center">
      <v-col cols="12">
        <v-card class="d-flex flex-wrap" flat>
          <template v-for="index in getNumberOfHouseholdMembers()">
            <v-card class="pa-2" flat min-width="33%" :key="index">
              <div class="font-weight-medium primary--text">
                Household Member #{{ index }}
              </div>
              <v-row>
                  <v-col cols="12"><div>
                    <vue-qrcode
                     :id="index - 1"
                     v-bind:value="qrValue[index - 1]"
                     v-bind:errorCorrectionLevel="correctionLevel"
                    /></div>
                  </v-col>
              </v-row>
              <div class="font-weight-medium">
                Name:
                <span class="font-weight-regular"
                  >{{ dataHouseholdPersonalInfo[index - 1].familyName }},
                  {{ dataHouseholdPersonalInfo[index - 1].givenName }}
                  {{ dataHouseholdPersonalInfo[index - 1].middleName }}
                  {{ dataHouseholdPersonalInfo[index - 1].suffix }}</span
                >
              </div>
            </v-card>
          </template>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <p></p>
      </v-col>
    </v-row>
    <v-row justify="center">
      <div class="font-weight-medium">
        <br /><br />How do you want to receive these QR codes?
      </div>
    </v-row>
    <v-row justify="center">
      <v-col cols="3" sm="3" md="3">
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
      dataHouseholdPersonalInfo: [],
      correctionLevel: "H",
      qrValue: [],
      sendQr: " ",
    };
  },
  props: {
    numberOfHouseholdMembers: Number,
  },
  methods: {
    generatePdf() {
      let pdfDoc = new jsPDF();
      pdfDoc.setFontSize(10);
      for (let i = 0; i < this.getNumberOfHouseholdMembers(); i += 2) {
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

        //add the first QR code and label
        const qrcode1 = document.getElementById(i);
        let imageData = this.getBase64Image(qrcode1);
        let string = this.dataHouseholdPersonalInfo[i].familyName + ", " + this.dataHouseholdPersonalInfo[i].givenName  + " " + this.dataHouseholdPersonalInfo[i].middleName  + " " + this.dataHouseholdPersonalInfo[i].suffix;
        pdfDoc.addImage(imageData, "JPG", 70, 50);
        pdfDoc.text(string, 75, 48);

        //add the second QR code and label, if necessary
        if (i + 1 < this.getNumberOfHouseholdMembers()) {
          const qrcode2 = document.getElementById(i + 1);
          imageData = this.getBase64Image(qrcode2);
          string = this.dataHouseholdPersonalInfo[i + 1].familyName + ", " + this.dataHouseholdPersonalInfo[i + 1].givenName  + " " + this.dataHouseholdPersonalInfo[i + 1].middleName  + " " + this.dataHouseholdPersonalInfo[i + 1].suffix;
          pdfDoc.addImage(imageData, "JPG", 70, 150);
          pdfDoc.text(string, 75, 148);
          if (i + 2 < this.getNumberOfHouseholdMembers()) {
            pdfDoc.addPage();
          }
        }
      }
      pdfDoc.save("COVID-19_Registration_QR_Code.pdf");
    },
    getBase64Image(img) {
      var canvas = document.createElement("canvas");
      canvas.width = 200;
      canvas.height = 200;
      var ctx = canvas.getContext("2d");
      ctx.drawImage(img, 0, 0);
      var dataURL = canvas.toDataURL("image/png");
      return dataURL;
    },
    updateHouseholdPersonalInfoData(
      householdPersonalInfoPayload,
      householdMemberNumber
    ) {
      // if member is next in line, add to array of members
      if (householdMemberNumber - 1 == this.dataHouseholdPersonalInfo.length) {
        this.dataHouseholdPersonalInfo.push(householdPersonalInfoPayload);
        // else if member already exists in array, update the member
      } else if (
        householdMemberNumber - 1 <
        this.dataHouseholdPersonalInfo.length
      ) {
        this.$set(
          this.dataHouseholdPersonalInfo,
          householdMemberNumber - 1,
          householdPersonalInfoPayload
        );
      }
    },
    getNumberOfHouseholdMembers() {
      return this.numberOfHouseholdMembers;
    },
    updateQrCodeData(data) {
      for(let i=0; i<data.length; i++)
      {
        this.qrValue.push(data[i])
      }
    },
  },
  components: {
    VueQrcode,
  },
  mounted() {
    EventBus.$on(
      "DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED",
      (householdPersonalInfoPayload, householdMemberNumber) => {
        this.updateHouseholdPersonalInfoData(
          householdPersonalInfoPayload,
          householdMemberNumber
        );
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