<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Patient History</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
      <PatientHistoryComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from "./PatientInfoComponent";
import PatientHistoryComponent from "./PatientHistoryComponent";

export default {
  name: "PatientHistoryPage",
  methods: {},
  components: {
    PatientInfoComponent,
    PatientHistoryComponent,
  },
  data() {
    return {};
  },
};
</script>
