<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Check-In</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
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
