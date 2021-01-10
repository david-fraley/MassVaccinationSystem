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
        <div class="font-weight-medium secondary--text">
          Retrieve the patient's record by scanning their QR code.
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent" @click="scanQrCode">
          Scan QR code
        </v-btn>
      </v-col>
    </v-row>
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

export default {
  name: "PatientLookupPage",
  methods: {
    searchPatient() {
      this.$refs.form.validate();
      if (!this.valid) return;

      this.patientLookupTable = [];
      this.loading = true;

      let data = {
        lastName: this.lastName,
        firstName: this.firstName,
        birthDate: this.birthDate,
        postalCode: this.postalCode,
      };
      brokerRequests.searchPatient(data).then((response) => {
        this.patientLookupTable = response.data;
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
      let qrValue = "example";
      let data = { id: qrValue };

      brokerRequests.getPatient(data).then((response) => {
        this.patient = response.data;
        this.patientRecordRetrieved();
      });
    },
  },
  components: {},
  data() {
    return {
      firstName: "",
      lastName: "",
      birthDate: "",
      postalCode: "",
      patient: "",
      loading: false,
      valid: false,

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
</style>
