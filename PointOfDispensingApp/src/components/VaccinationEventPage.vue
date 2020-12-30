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
      <VaccinationScreeningComponent @VaccinationProceed_Yes="setVaccinationProceedDecision(true)" @VaccinationProceed_No="setVaccinationProceedDecision(false)"/>
      <template v-if="wasDecisionMadeToProceed()">
      <VaccinationProceedComponent />
      </template>
      <template v-else-if="wasDecisionMadeToCancel()">
      <VaccinationCanceledComponent />
      </template>
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import VaccinationScreeningComponent from './VaccinationScreeningComponent';
import VaccinationProceedComponent from './VaccinationProceedComponent';
import VaccinationCanceledComponent from './VaccinationCanceledComponent';

  export default {
    name: 'VaccinationEventPage',
    methods: 
    {
      setVaccinationProceedDecision(decision)
      {
        decision ? (this.vaccinationProceedDecision = 'Yes') :
        (this.vaccinationProceedDecision = 'No');
      },
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
      VaccinationScreeningComponent,
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
