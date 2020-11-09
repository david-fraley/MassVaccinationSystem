<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        Reason vaccine was not administered goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4">
        Vaccination status (e.g. not done) goes here
      </v-col>
      <v-col cols="4">
        Vaccination time stamp goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Healthcare practitioner goes here
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
    name: 'VaccinationCanceled',
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
