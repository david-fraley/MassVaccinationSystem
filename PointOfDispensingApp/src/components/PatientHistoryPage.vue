<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Patient History</v-toolbar-title>
    </v-toolbar>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-divider></v-divider>
        </v-col>
      </v-row>
      <v-row>
        <PatientInfoComponent />
        <PatientHistoryComponent />
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import PatientHistoryComponent from './PatientHistoryComponent';

  export default {
    name: 'PatientHistoryPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      PatientHistoryComponent
    },
    data () {
      return {
      }
    }
  }
</script>
