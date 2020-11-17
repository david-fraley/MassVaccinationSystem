<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Adverse Reaction</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
      <AdverseEventsComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from "./PatientInfoComponent";
import AdverseEventsComponent from "./AdverseEventsComponent";

export default {
  name: "AdverseEventsPage",
  methods: {},
  components: {
    PatientInfoComponent,
    AdverseEventsComponent,
  },
  data() {
    return {};
  },
};
</script>
