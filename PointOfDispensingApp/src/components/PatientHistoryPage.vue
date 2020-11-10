<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
    </v-row>
    <v-row>
      <PatientHistoryComponent />
    </v-row>
  </v-container>
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
