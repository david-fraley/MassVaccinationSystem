<template>
  <v-container>
    <v-row>
        <v-col cols="12">
          <p>Retrieve the patient's record by scanning their QR code.</p>
        </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Scan QR code
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <p>If the QR code is not available, search for the patient record by name and/or DOB.</p>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4">
        Last name goes here
      </v-col>
      <v-col cols="4">
        First name goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Date of Birth goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Search
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Lookup table goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Retrieve patient record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'PatientLookup',
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
