<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Vaccination status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="immunizationStatus"
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="immunizationTimeStamp"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="encounter.status"
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="encounter.end"
        ></v-text-field>
      </v-col>
    </v-row>
    <!-- <v-row>
      <v-col cols="2">
      </v-col>    
      <v-col cols="6">
        <v-btn block color="accent" @click="endEncounter()">
          End encounter
        </v-btn>
      </v-col>
    </v-row> -->
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">
          Would you like to print or e-mail the patient summary?
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"></v-col>
      <v-col cols="3">
        <v-btn block color="accent">
          Print
        </v-btn>
      </v-col>
      <v-col cols="3">
        <v-btn block color="accent">
          E-mail
        </v-btn>
      </v-col>
      <v-col cols="2"></v-col>
    </v-row>
  </v-container>
</template>

<script>
import brokerRequests from "../brokerRequests";

export default {
  name: "PatientDischargeComponent",
  computed: {
    immunizationStatus() {
      return this.$store.state.immunizationResource.immunizationStatus;
    },
    immunizationTimeStamp() {
      return this.$store.state.immunizationResource.immunizationTimeStamp;
    },
    encounter() {
      return this.$store.state.encounterResource;
    },
    appointment() {
      return this.$store.state.appointmentResource;
    },
  },
  methods: {
    onSuccess(payload) {
      this.$store.dispatch("patientDischarged", payload);
    },
    discharge() {
      let data = {
        apptID: this.appointment.id,
        encounterID: this.encounter.id,
      };
      brokerRequests.discharge(data).then((response) => {
        if (response.data) {
          this.onSuccess(response.data);
        } else if (response.error) {
          console.log(response.error);
          alert(`Patient not discharged`);
        }
      });
    },
  },
  mounted() {
    this.discharge();
  },
  data() {
    return {};
  },
};
</script>
