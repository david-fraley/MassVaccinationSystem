<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
    </v-row>
    <v-row>
      <AdverseEventsComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import AdverseEventsComponent from './AdverseEventsComponent';

  export default {
    name: 'AdverseEventsPage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      AdverseEventsComponent
    },
    data () {
      return {
      }
    }
  }
</script>
