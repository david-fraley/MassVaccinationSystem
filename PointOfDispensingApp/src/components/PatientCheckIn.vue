<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium primary--text">Location</div>
      </v-col>
      <v-col cols="10">
        <v-row>
          <v-col cols="4">
            POD identifier goes here
          </v-col>
          <v-col cols="4">
            POD name goes here
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12">
            <v-btn color="accent">
              Change location...
            </v-btn>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium primary--text">Encounter status</div>
      </v-col>
      <v-col cols="10">
        <v-row>
          <v-col cols="4">
            Encounter status (e.g. "arrived") goes here
          </v-col>
          <v-col cols="4">
            Encounter time stamp goes here
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12">
            <v-btn color="accent">
              Patient information verified
            </v-btn>
          </v-col>
        </v-row>
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
