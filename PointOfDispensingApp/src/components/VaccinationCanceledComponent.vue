<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        Reason vaccine was not administered
      </v-col>
      <v-col cols="4">
        <v-select
          :items="vaccineNotAdministeredOptions"
          outlined
          dense
          required
          :rules="[(v) => !!v || 'Reason field is required']"
          v-model="notAdministeredReason"
        ></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        Vaccination status
      </v-col>
      <v-col cols="4">
        <v-text-field
          outlined
          dense
          filled
          readonly
          :value="immunizationStatus"
        ></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-text-field
          outlined
          dense
          filled
          readonly
          :value="immunizationTimeStamp"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        Healthcare practitioner
      </v-col>
      <v-col cols="4">
        <v-text-field
          outlined
          dense
          filled
          readonly
          :value="healthcarePractitioner"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          placeholder="Notes"
          outlined
          rows="4"
          :value="notes"
          v-model="notes"
        ></v-textarea>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent" @click="submitVaccinationRecord(); endEncounter();">
          Submit vaccination record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import brokerRequests from "../brokerRequests";

export default {
  name: "VaccinationCanceledComponent",
  computed: {
    immunizationStatus() {
      return this.$store.state.immunizationResource.immunizationStatus;
    },
    immunizationTimeStamp() {
      return this.$store.state.immunizationResource.immunizationTimeStamp;
    },
    healthcarePractitioner() {
      return this.$store.state.immunizationResource.healthcarePractitioner;
    },
  },
  methods: {
    onSuccessSubmitVaccinationRecord() {
      const vaccinationCanceledPlayload = {
        immunizationStatus: "Not-done",
        immunizationTimeStamp: new Date().toISOString(),
        healthcarePractitioner: this.healthcarePractitioner,
        notAdministeredReason: this.notAdministeredReason,
        notes: this.notes,
        };

        //send data to Vuex
        this.$store.dispatch("vaccinationCanceled", vaccinationCanceledPlayload);

        //Advance to the Discharge page
        this.$router.push("Discharge");

        //Close the dialog
        this.dialog = false;
    },
    onSuccessEndEncounter() {
      //the following is sending dummy data until we have the API in place
      const encounterResourcePayload = {
        encounterStatus: "Finished",
        encounterTimeStamp: new Date().toISOString(),
      };
      //send data to Vuex
      this.$store.dispatch("patientDischarged", encounterResourcePayload);
    },
    submitVaccinationRecord() {
      brokerRequests.submitVaccination().then((response) => {
        if (response.data) {
          this.onSuccessSubmitVaccinationRecord();
        } else if (response.error) {
          alert("Vaccination record not submitted");
        }
      });
    },
    endEncounter() {
      brokerRequests.discharge().then((response) => {
      if (response.data) {
        this.onSuccessEndEncounter();
      } else if (response.error) {
        alert("Discharge not successful");
      }
    });
  },
  },
  components: {},
  data() {
    return {
      dialog: false,
      vaccineNotAdministeredOptions: [
        "Medical precaution",
        "Immune",
        "Out of Stock",
        "Patient Objection",
      ],
      notAdministeredReason: this.$store.state.immunizationResource
        .notAdministeredReason,
      notes: this.$store.state.immunizationResource.notes,
    };
  },
};
</script>
