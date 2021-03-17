<template>
  <v-card color="white" elevation="0" tile>
    <v-toolbar color="secondary">
      <v-toolbar-title class="font-weight-medium white--text">Retrieve Patient Record</v-toolbar-title>
    </v-toolbar>
    <v-container>
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
      <v-btn @click="scanQrCode()" color="secondary"> Scan QR Code </v-btn>
      </v-col>
      <v-col cols="6">
      <div v-if="isCameraOn && !noFrontCamera && !noRearCamera">
        <v-btn @click="switchCamera" color="secondary"> Switch Camera </v-btn>
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
        <v-row no-gutters>
          <v-col cols="2">
            <div class="secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              outlined
              dense
              v-model="lastName"
              required
              :rules="[(v) => !!v || 'Last Name is required']"
            ></v-text-field>
          </v-col>
          <v-spacer></v-spacer>
          <v-col cols="2">
            <div class="secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              outlined
              dense
              v-model="firstName"
              required
              :rules="[(v) => !!v || 'First Name is required']"
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-col cols="2">
            <div class="secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
          <v-text-field
              outlined
              dense
              v-model="dateFormatted"
              required
              :rules="birthdateRules"
              placeholder="MM/DD/YYYY"
              v-mask="'##/##/####'"
          ></v-text-field>        
          </v-col>
          <v-spacer></v-spacer>
          <v-col cols="2">
            <div class="secondary--text">Zip Code (optional)</div>
          </v-col>
          <v-col cols="3">
            <v-text-field 
              outlined 
              dense 
              :rules="postalCodeRules"
              v-model="postalCode">
            </v-text-field>
          </v-col>
        </v-row>
          <v-card-actions>
            <v-btn left color="secondary" @click="searchPatient">
              Search
            </v-btn>
            <v-btn right color="secondary" @click="clear" outlined>
              Clear Info
            </v-btn>
          </v-card-actions>
          <br/>
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
          <v-btn color="secondary" @click="retrievePatientRecord">
            Retrieve patient record
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
 
</template>

<script>
import brokerRequests from "../brokerRequests";
import {QrcodeStream} from "vue-qrcode-reader";

export default {
  name: "PatientLookupPage",
  methods: {
    clear () {
        this.$refs.form.reset()
      },
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

      //Interim solution:  extract screening responses from QR code (part 1 of 2)
      var res = this.result.split("|");
      //end of interim solution (part 1 of 2)

      //the following line of code may need to be updated after removing/changing the interim solution"
      let qrValue = res[0];

      // result should be null if we didn't get a qrCode
      brokerRequests.getPatientFromQrCode(qrValue).then((response) => {
        if (response.patient) {
          this.patient = response.patient;
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
        birthDate: this.parseDate(this.birthdate),
        postalCode: this.postalCode,
      };
      brokerRequests.searchPatient(data).then((response) => {
        if (response.patients) {
          this.patientLookupTable = response.patients;
        }

        // Accomodate form validation errors
        else if(typeof(response.error) === 'string') {
          alert(response.error.error);
        }

        // Accomodate FHIR errors
        else {
          alert('No patients found');
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
    async getPayload() {
      let payload = { Patient: this.patient };

      // Get Appointment, Encounter, Immunization
      let immunizationPromise = brokerRequests
        .getImmunization(this.patient.id)
        .then((response) => {
          if (response.data) {
            payload.Immunization = response.data;
          } else if (response.error) {
            console.log(response.error);
            console.log("Failed to load history");
          }
        });

      let encounterPromise = brokerRequests
        .getEncounter(this.patient.id)
        .then((response) => {
          if (response.data) {
            payload.Encounter = response.data;
          } else if (response.error) {
            console.log(response.error);
            console.log("Failed to get Encounter");
          }
        });

      let appointmentPromise = brokerRequests
        .getAppointment(this.patient.id)
        .then((response) => {
          if (response.data) {
            payload.Appointment = response.data;
          } else if (response.error) {
            console.log(response.error);
            alert("Failed to get Appointment");
          }
        });

      await Promise.all([immunizationPromise, encounterPromise, appointmentPromise]).catch(
        (error) => {
          console.log(error);
          console.log("PatientLookupPage patientRecordRetrieved error");
        }
      );

      return payload;
    },
    patientRecordRetrieved() {
      this.getPayload().then((payload) => {
        //send data to Vuex
        this.$store.dispatch("patientRecordRetrieved", payload);

        //Interim solution:  extract screening responses from QR code (part 2 of 2)
        var res = this.result.split("|");
        if(res.length > 1)
        {
          const screeningResponsesPayload = {
          vaccinationDecision: '',
          screeningQ1: res[1],
          screeningQ2: res[2],
          screeningQ2b: res[3],
          screeningQ3a: res[4],
          screeningQ3b: res[5],
          screeningQ3c: res[6],
          screeningQ4: res[7],
          screeningQ5: res[8],
          screeningQ6: res[9],
          screeningQ7: res[10],
          screeningQ8: res[11],
          screeningComplete: 'true'
          }
          //send data to Vuex
          this.$store.dispatch('vaccinationScreeningUpdate', screeningResponsesPayload)
        }
        //end of interim solution (part 2 of 2)

        //Determine how many doses the patient has received, and set color scheme accordingly
        let administeredDoses = this.$store.getters.howManyDosesHasPatientReceived;
        
        if(administeredDoses == 0) {
          //blue color scheme
          this.$vuetify.theme.themes.light.primary = '#1976D2';
          this.$vuetify.theme.themes.light.accent = '#64B5F6';
          this.$vuetify.theme.themes.light.pageColor = '#E3F2FD';
        }
        else if(administeredDoses == 1) {
          //orange color scheme
          this.$vuetify.theme.themes.light.primary = '#F57C00'
          this.$vuetify.theme.themes.light.accent = '#FFB74D'
          this.$vuetify.theme.themes.light.pageColor = '#FFF3E0'
        }
        else {
          //purple color scheme
          this.$vuetify.theme.themes.light.primary = '#7C4DFF'
          this.$vuetify.theme.themes.light.accent = '#B388FF'
          this.$vuetify.theme.themes.light.pageColor = '#EDE7F6'
        }
        
        if(this.$store.getters.hasPatientBeenCheckedIn) {
          //Advance to the Consent and Screening page
          this.$router.push("ConsentScreening");
        }
        else {
          //Advance to the Check In page
          this.$router.push("CheckIn");
        }
      });
    },
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var formattedDate = this.parseDate(birthdate);
      var date = Date.parse(formattedDate);

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    parseDate(date) {
      if (!date) return null;
      // Ensure birthdate is fully entered and can be converted into 3 variables before parsing
      if (date.split("/").length !== 3) return null;

      const [month, day, year] = date.split("/");
      return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
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
      postalCode: "",
      patient: "",
      loading: false,
      valid: false,
      camera: 'rear',
      isCameraOn: false,
      noRearCamera: false,
      noFrontCamera: false,
      result: '',
      minDateStr: "1900-01-01",
      birthdateRules: [
        (v) => !!v || "DOB is required",
        // check if v exists before seeing if the length is 10
        (v) =>
          (!!v && v.length === 10) ||
          "DOB must be in specified format, MM/DD/YYYY",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
      postalCodeRules: [
				(v) =>
				!v || /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) || 
				"Zip code format must be ##### or #####-####",
      ],
      birthdate: "",
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
  computed: {
    maxDateStr: function() {
      let d = new Date();
      let date = [
        d.getFullYear(),
        ("0" + (d.getMonth() + 1)).slice(-2),
        ("0" + d.getDate()).slice(-2),
      ].join("-");

      return date;
    },
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