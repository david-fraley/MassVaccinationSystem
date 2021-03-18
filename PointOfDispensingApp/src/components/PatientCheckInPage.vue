<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Check-In</v-toolbar-title>
    </v-toolbar>
    <v-container>
      <v-row>
        <PatientInfoComponent />
        <v-col>
          <h3 class="font-weight-medium primary--text">Vaccination History</h3>
        </v-col>
        <PatientHistoryComponent />
        <v-col cols="12">
          <v-divider></v-divider>
        </v-col>
        <v-col>
          <h3 class="font-weight-medium primary--text">Check-in</h3>
        </v-col>
        <PatientCheckInComponent />
      </v-row>
    </v-container>
  </v-card>
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
