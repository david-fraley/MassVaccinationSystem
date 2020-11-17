<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Discharge</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <PatientInfoComponent />
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
