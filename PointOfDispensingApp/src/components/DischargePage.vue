<template>
  <v-card color="pageColor" elevation="0" tile>
    <v-toolbar color="primary">
      <v-toolbar-title class="font-weight-medium white--text">Discharge</v-toolbar-title>
    </v-toolbar>
    <v-container>
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
  </v-card>
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
