<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Scan vaccine barcode
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-row>
          Lot number goes here
        </v-row>
        <v-row>
          Expiration date goes here
        </v-row>
        <v-row>
          Manufacturer goes here
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          Dose quantity goes here
        </v-row>
        <v-row>
          Dose number goes here
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Vaccine administered
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-row>
          Vaccination status goes here
        </v-row>
        <v-row>
          Healthcare practitioner goes here
        </v-row>
        <v-row>
          Site of vaccination goes here
        </v-row>
        <v-row>
         Route goes here
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          Vaccination time stamp goes here
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Notes box goes here
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent">
          Submit vaccination record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'VaccinationProceed',
    methods: 
    {
    },
    components: 
    {
    },
    data () {
      return {
      }
    }
  }
</script>
