<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Consent and Screening</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
      <VaccinationScreeningComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from "./PatientInfoComponent";
import VaccinationScreeningComponent from "./VaccinationScreeningComponent";

export default {
  name: "ConsentScreeningPage",
  methods: {},
  components: {
    PatientInfoComponent,
    VaccinationScreeningComponent,
  },
  data() {
    return {};
  },
};
</script>
