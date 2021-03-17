<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Check-In</v-toolbar-title>
    </v-toolbar>
    <v-container>
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
  </v-card>
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
