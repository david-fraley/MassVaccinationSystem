<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
      <EditPatientComponent />
      <v-col>
        <h2 class="font-weight-medium primary--text">Vaccination History</h2>
      </v-col>
      <PatientHistoryComponent />
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
      <v-col>
        <h2 class="font-weight-medium primary--text">Check-in</h2>
      </v-col>
      <PatientCheckInComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import EditPatientComponent from './EditPatientComponent';
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
      EditPatientComponent,
      PatientHistoryComponent,
      PatientCheckInComponent
    },
    data () {
      return {
      }
    }
  }
</script>
