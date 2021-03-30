<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Vaccination Event</v-toolbar-title>
    </v-toolbar>
    <v-container>
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
          <div class="font-weight-medium secondary--text">Please review and complete the screening questions.</div>
        </template>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import VaccinationProceedComponent from './VaccinationProceedComponent';
import VaccinationCanceledComponent from './VaccinationCanceledComponent';

  export default {
    name: 'VaccinationEventPage',
    methods: 
    {
      wasDecisionMadeToProceed()
      {
        return (this.vaccinationProceedDecision=='Yes')
      },
      wasDecisionMadeToCancel()
      {
        return (this.vaccinationProceedDecision=='No')
      }
    },
    components: 
    {
      PatientInfoComponent,
      VaccinationProceedComponent,
      VaccinationCanceledComponent,
    },
    data () {
      return {
        vaccinationProceedDecision: this.$store.state.screeningResponses.vaccinationDecision
      }
    }
  }
</script>
