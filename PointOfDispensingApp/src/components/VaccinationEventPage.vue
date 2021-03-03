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
      <template v-if="wasDecisionMadeToProceed()">
        <VaccinationProceedComponent />
      </template>
      <template v-else-if="wasDecisionMadeToCancel()">
        <VaccinationCanceledComponent />
      </template>
      <template v-else>
        <div class="font-weight-medium secondary--text">
          Please review and complete the screening questions.
        </div>
      </template>
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from "./PatientInfoComponent";
import VaccinationProceedComponent from "./VaccinationProceedComponent";
import VaccinationCanceledComponent from "./VaccinationCanceledComponent";

export default {
  name: "VaccinationEventPage",
  methods: {
    wasDecisionMadeToProceed() {
      return this.vaccinationProceedDecision == "Yes";
    },
    wasDecisionMadeToCancel() {
      return this.vaccinationProceedDecision == "No";
    },
  },
  components: {
    PatientInfoComponent,
    VaccinationProceedComponent,
    VaccinationCanceledComponent,
  },
  data() {
    return {
      vaccinationProceedDecision: this.$store.state.screeningResponses
        .vaccinationDecision,
    };
  },
};
</script>
