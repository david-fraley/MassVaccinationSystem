<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Retrieve Patient Record</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row> 
        <v-col cols="12">
          <div class="font-weight-medium secondary--text">Retrieve the patient's record by scanning their QR code.</div>
        </v-col>
    </v-row>
    
  <template>
  <v-row><div>
    <p class="error" v-if="isCameraOn && noFrontCamera">
      You don't seem to have a front camera on your device
    </p>

    <p class="error" v-if="isCameraOn && noRearCamera">
      You don't seem to have a rear camera on your device
    </p></div></v-row>
  <v-row>
    <v-col cols="6">
    <v-btn @click="scanQrCode()" color="accent"> Scan QR Code </v-btn>
    </v-col>
    <v-col cols="6">
    <div v-if="isCameraOn && !noFrontCamera && !noRearCamera">
      <v-btn @click="switchCamera" color="accent"> Switch Camera </v-btn>
    </div>
    </v-col>
  </v-row>
  <div v-if="isCameraOn"><v-row><v-col cols="6"><qrcode-stream :camera="camera" @init="onInit" @decode="onDecode">
    </qrcode-stream></v-col></v-row>
    <v-row><p class="decode-result"> Result: <b>{{result}}</b></p></v-row>
  </div>
  </template>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">
          If the QR code is not available, search for the patient record by name
          and/or DOB.
        </div>
      </v-col>
    </v-row>

    <v-form ref="form" v-model="valid">
      <v-row>
        <v-col cols="5">
          <v-row no-gutters>
            <v-col cols="4">
              <div class="secondary--text">Last Name</div>
            </v-col>
            <v-col cols="8">
              <v-text-field
                outlined
                dense
                v-model="lastName"
                required
                :rules="[(v) => !!v || 'Last Name is required']"
              ></v-text-field>
            </v-col>
            <v-col cols="4">
              <div class="secondary--text">Date of Birth</div>
            </v-col>
            <v-col cols="8">
              <v-text-field
                outlined
                dense
                v-model="birthDate"
                placeholder="YYYY-MM-DD"
                required
                :rules="[(v) => !!v || 'Date of Birth is required']"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-col>
        <!--blank column for spacing-->
        <v-col cols="1"> </v-col>
        <v-col cols="5">
          <v-row no-gutters>
            <v-col cols="4">
              <div class="secondary--text">First Name</div>
            </v-col>
            <v-col cols="8">
              <v-text-field
                outlined
                dense
                v-model="firstName"
                required
                :rules="[(v) => !!v || 'First Name is required']"
              ></v-text-field>
            </v-col>
            <v-col cols="4">
              <div class="secondary--text">Zip Code</div>
            </v-col>
            <v-col cols="8">
              <v-text-field outlined dense v-model="postalCode"></v-text-field>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <v-row no-gutters>
        <v-col cols="12">
          <v-btn color="accent" @click="searchPatient">
            Search
          </v-btn>
        </v-col>
      </v-row>
    </v-form>
    <v-row>
      <v-col cols="12">
        <template>
          <v-data-table
            @click:row="clickRow"
            @dblclick:row="dblclickRow"
            item-key="id"
            single-select
            :headers="headers"
            disable-sort
            :items="patientLookupTable"
            class="elevation-1"
            :loading="loading"
            :footer-props="{
              'items-per-page-options': [5],
            }"
            :items-per-page="5"
          ></v-data-table>
        </template>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent" @click="retrievePatientRecord">
          Retrieve patient record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import brokerRequests from "../brokerRequests";
import {QrcodeStream} from "vue-qrcode-reader";
export default {
  name: "PatientLookupPage",
  methods: {
    toggleCamera () {
      this.isCameraOn = !this.isCameraOn;
    },
    switchCamera () {
      switch (this.camera) {
        case 'front':
          this.camera = 'rear'
          break
        case 'rear':
          this.camera = 'front'
          break
      }
    },
    async onInit (promise) {
      try {
        await promise
      } catch (error) {
        const triedFrontCamera = this.camera === 'front'
        const triedRearCamera = this.camera === 'rear'
        const cameraMissingError = error.name === 'OverconstrainedError'
        if (triedRearCamera && cameraMissingError) {
          this.noRearCamera = true
          this.camera = 'front'
        }
        if (triedFrontCamera && cameraMissingError) {
          this.noFrontCamera = true
          this.camera = 'rear'
        }
        console.error(error)
      }
    },
      onDecode (result) {
        this.result = result
        let qrValue = "example";
        let data = { id: qrValue };
        brokerRequests.getPatient(data).then((response) => {
          if (response.data) {
            this.patient = response.data;
            this.patientRecordRetrieved();
          } else if (response.error) {
            alert("Patient not found");
          }
        });
      },
    searchPatient() {
      // validate form
      this.$refs.form.validate();
      if (!this.valid) return;
      // update for loading animation
      this.patientLookupTable = [];
      this.loading = true;
      // make request
      let data = {
        lastName: this.lastName,
        firstName: this.firstName,
        birthDate: this.birthDate,
        postalCode: this.postalCode,
      };
      brokerRequests.searchPatient(data).then((response) => {
        if (response.data) {
          this.patientLookupTable = response.data;
        } else if (response.error) {
          alert("No patients found");
        }
        this.loading = false;
      });
    },
    clickRow(item, row) {
      row.select(true);
      this.patient = item;
    },
    dblclickRow() {
      this.patientRecordRetrieved();
    },
    retrievePatientRecord() {
      if (this.patient) {
        this.patientRecordRetrieved();
      } else {
        alert("Select a patient");
      }
    },
    patientRecordRetrieved() {
      //send data to Vuex
      this.$store.dispatch("patientRecordRetrieved", this.patient);
      //Advance to the Check In page
      this.$router.push("CheckIn");
    },
    scanQrCode() {
      this.toggleCamera()
    },
  },
  components: {
    QrcodeStream
  },
  data() {
    return {
      firstName: "",
      lastName: "",
      birthDate: "",
      postalCode: "",
      patient: "",
      loading: false,
      valid: false,
      camera: 'rear',
      isCameraOn: false,
      noRearCamera: false,
      noFrontCamera: false,
      result: '',
      headers: [
        {
          text: "Last Name",
          align: "start",
          value: "family",
        },
        { text: "First Name", value: "given" },
        { text: "DOB", value: "birthDate" },
        { text: "Address", value: "address.line" },
        { text: "City", value: "address.city" },
        { text: "Zipcode", value: "address.postalCode" },
      ],
      patientLookupTable: [],
    };
  },
};
</script>
<style>
tr.v-data-table__selected {
  background: #1976d2 !important;
  color: #ffffff;
}
.error {
    font-weight: medium;
}
</style>