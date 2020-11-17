<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Vaccination Event</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
      <VaccinationScreeningComponent />
      <template v-if="true">
        <VaccinationProceedComponent />
      </template>
      <template v-if="false">
        <VaccinationCanceledComponent />
      </template>
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from "./PatientInfoComponent";
import VaccinationScreeningComponent from "./VaccinationScreeningComponent";
import VaccinationProceedComponent from "./VaccinationProceedComponent";
import VaccinationCanceledComponent from "./VaccinationCanceledComponent";

export default {
  name: "VaccinationEventPage",
  methods: {},
  components: {
    PatientInfoComponent,
    VaccinationScreeningComponent,
    VaccinationProceedComponent,
    VaccinationCanceledComponent,
  },
  data() {
    return {};
  },
};
</script>
