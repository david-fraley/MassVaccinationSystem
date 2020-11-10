<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
    </v-row>
    <v-row>
      <VaccinationScreeningComponent />
    </v-row>
    <v-row>
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
import PatientInfoComponent from './PatientInfoComponent';
import VaccinationScreeningComponent from './VaccinationScreeningComponent';
import VaccinationProceedComponent from './VaccinationProceedComponent';
import VaccinationCanceledComponent from './VaccinationCanceledComponent';

  export default {
    name: 'VaccinationEventPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      VaccinationScreeningComponent,
      VaccinationProceedComponent,
      VaccinationCanceledComponent
    },
    data () {
      return {
      }
    }
  }
</script>
