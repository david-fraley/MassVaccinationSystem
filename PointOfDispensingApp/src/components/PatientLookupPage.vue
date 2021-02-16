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
        <!-- Date of Birth -->
          <v-menu
            attach
            :close-on-content-click="false"
            transition="scale-transition"
            offset-y
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                outlined dense 
                v-model="dateFormatted"
                :rules="birthdateRules"
                placeholder="MM/DD/YYYY"
                v-mask="'##/##/####'"
                prepend-icon="mdi-calendar"
                @click:prepend="on.click"
                @blur="date = parseDate(dateFormatted)"
              >
              </v-text-field>
            </template>
            <v-date-picker
              reactive
              v-model="date"
            ></v-date-picker>
          </v-menu>
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
  watch: {
      date () {
        this.dateFormatted = this.formatDate(this.date)
      },
  },
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

        //Interim solution:  extract screening responses from QR code (part 1 of 2)
        var res = this.result.split("|");
        //end of interim solution (part 1 of 2)

        //the following line of code may need to be updated after removing/changing the interim solution"
        let qrValue = res[0];
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
        birthDate: this.date,
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
    },
    formatDate (date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${month}/${day}/${year}`
    },
    parseDate (date) {
      if (!date) return null

      const [month, day, year] = date.split('/')
      return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
    },
    scanQrCode() {
      this.toggleCamera()
    },
calculatedAge(patientBirthday) {
  
  // reference: https://stackoverflow.com/questions/12251325/javascript-date-to-calculate-age-work-by-the-day-months-years

  var now = new Date();
  var yearNow = now.getYear();
  var monthNow = now.getMonth();
  var dateNow = now.getDate();

  patientBirthday = new Date(patientBirthday.substring(6,10), patientBirthday.substring(0,2)-1, patientBirthday.substring(3,5));

  var patientYear = patientBirthday.getYear();
  var patientMonth = patientBirthday.getMonth();
  var patientDate = patientBirthday.getDate();

  var yearAge = yearNow - patientYear;
  var monthAge;
  var dateAge;

  if (monthNow >= patientMonth)
    monthAge = monthNow - patientMonth;
  else {
    yearAge--;
    monthAge = 12 + monthNow -patientMonth;
  }

  if (dateNow >= patientDate)
    dateAge = dateNow - patientDate;
  else {
    monthAge--;
    dateAge = 31 + dateNow - patientDate;

    if (monthAge < 0) {
      monthAge = 11;
      yearAge--;
    }
  }
  
  var ageString = yearAge + " yy " + monthAge + " mm "+ dateAge + " dd"; 

  return ageString;
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
      patientAge: '',
      birthdateRules: [
        (v) => v.length == 10 || "DOB must be in format MM/DD/YYYY"
      ],
      postalCodeRules: [
				(v) => !!v || "Zip code is required",
				(v) =>
				/(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) ||
				"Zip code format must be ##### or #####-####",
      ],
      date: new Date().toISOString().substr(0, 10),
      dateFormatted: "",
      headers: [
        {
          text: "Last Name",
          align: "start",
          value: "family",
        },
        { text: "First Name", value: "given" },
        { text: "Age", value: "patientAge"},
        { text: "DOB", value: "birthDate" },
        { text: "Address", value: "address.line" },
        { text: "City", value: "address.city" },
        { text: "Zipcode", value: "address.postalCode" },
      ],
      patientLookupTable: [
        {
        
				patientAge: this.calculatedAge(this.birthDate = '10/10/1899'),
				
			},	
      ],
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