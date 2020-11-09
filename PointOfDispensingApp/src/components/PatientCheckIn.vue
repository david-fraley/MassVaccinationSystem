<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <v-img 
          max-height="220" 
          max-width="200"
          src="../assets/blankPicture.png">
        </v-img>
      </v-col>
      <v-col cols="8">
        <v-row>
          Patient ID goes here
        </v-row>
        <v-row>
          Last Name goes here
        </v-row>
        <v-row>
          First Name goes here
        </v-row>
        <v-row>
          Date of Birth goes here
        </v-row>
        <v-row>
          Gender goes here
        </v-row>
        <v-row>
          Street Address goes here
        </v-row>
        <v-row>
          Language goes here
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'PatientCheckIn',
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
