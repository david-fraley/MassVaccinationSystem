<template>
  <v-container>
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
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Adverse reactions inputs go here
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent">
          Submit observation record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'AdverseEvents',
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
