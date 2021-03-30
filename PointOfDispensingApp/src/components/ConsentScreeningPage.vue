<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Consent and Screening</v-toolbar-title>
    </v-toolbar>
    <v-container>
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
  </v-card>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import VaccinationScreeningComponent from './VaccinationScreeningComponent';

  export default {
    name: 'ConsentScreeningPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      VaccinationScreeningComponent
    },
    data () {
      return {
      }
    }
  }
</script>
