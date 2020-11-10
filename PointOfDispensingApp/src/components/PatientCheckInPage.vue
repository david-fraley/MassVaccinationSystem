<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
    </v-row>
    <v-row>
      <PatientCheckInComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import PatientCheckInComponent from './PatientCheckInComponent';

  export default {
    name: 'PatientCheckInPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      PatientCheckInComponent
    },
    data () {
      return {
      }
    }
  }
</script>
