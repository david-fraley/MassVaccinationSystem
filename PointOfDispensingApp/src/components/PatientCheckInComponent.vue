<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Location</div>
      </v-col>
      <v-col cols=4>
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="locationId"
        ></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="locationName"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="4">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="encounterStatus"
        ></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-text-field
          filled
          dense
          readonly
          outlined
          :value="encounterTimeStamp"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="3"> </v-col>
      <v-col cols="6">
        <v-btn block color="accent" @click="checkIn()" :disabled="isCheckInPageReadOnly">
          Confirm Patient & Check-in Patient
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import brokerRequests from "../brokerRequests";

export default {
  name: "PatientCheckInComponent",
  computed: {
    appointmentId() {
      return this.$store.state.appointmentResource.id;
    },
    patientId() {
      return this.$store.state.patientResource.id;
    },
    locationId() {
      return this.$store.state.locationResource.id;
    },
    locationName() {
      return this.$store.state.locationResource.name;
    },
    encounterStatus() {
      return this.$store.state.encounterResource.status;
    },
    encounterTimeStamp() {
      return this.$store.state.encounterResource.start;
    },
    isCheckInPageReadOnly() {
      return this.$store.getters.isCheckInPageReadOnly
    },
  },
  methods: {
    onSuccess(payload) {
      //send data to Vuex
      this.$store.dispatch("patientAdmitted", payload);
    },
    checkIn() {
      let data = {
        status: "arrived",
        appointment: `Appointment/${this.appointmentId}`,
        patient: `Patient/${this.patientId}`,
        location: `Location/${this.locationId}`
      }
      // make request
      brokerRequests.checkIn(data).then((response) => {
        if (response.data) {
          this.onSuccess(response.data);
        } else if (response.error) {
          console.log(response.error);
          alert(`Patient not checked-in`);
        }
      });
    },
  },
  components: {},
  data() {
    return {};
  }
};
</script>
