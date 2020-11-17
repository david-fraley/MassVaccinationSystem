<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Retrieve Patient Record</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
        <v-col cols="12">
          <div class="font-weight-medium secondary--text">Retrieve the patient's record by scanning their QR code.</div>
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
        <div class="font-weight-medium secondary--text">If the QR code is not available, search for the patient record by name and/or DOB.</div>
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
    name: 'PatientLookupPage',
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
