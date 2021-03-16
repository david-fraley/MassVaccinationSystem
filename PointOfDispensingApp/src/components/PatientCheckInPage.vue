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
      <PatientHistoryComponent />
      <PatientCheckInComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import PatientHistoryComponent from './PatientHistoryComponent';
import PatientCheckInComponent from './PatientCheckInComponent';

  export default {
    name: 'PatientCheckInPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      PatientHistoryComponent,
      PatientCheckInComponent
    },
    data () {
      return {
      }
    }
  }
</script>
