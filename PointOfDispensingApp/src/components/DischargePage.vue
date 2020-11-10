<template>
  <v-container>
    <v-row>
      <PatientInfoComponent />
    </v-row>
    <v-row>
      <PatientDischargeComponent />
    </v-row>
  </v-container>
</template>

<script>
import PatientInfoComponent from './PatientInfoComponent';
import PatientDischargeComponent from './PatientDischargeComponent';

  export default {
    name: 'DischargePage',
    methods: 
    {
    },
    components: 
    {
      PatientInfoComponent,
      PatientDischargeComponent
    },
    data () {
      return {
      }
    }
  }
</script>
