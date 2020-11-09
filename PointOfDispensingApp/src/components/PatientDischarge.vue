<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium primary--text">Vaccination status</div>
      </v-col>
      <v-col cols="10">
        <v-row>
          <v-col cols="4">
            Vaccination status goes here
          </v-col>
          <v-col cols="4">
            Vaccination time stamp goes here
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
            Vaccination status goes here
          </v-col>
          <v-col cols="4">
            Vaccination time stamp goes here
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="8">
            <v-btn block color="accent">
              End encounter
            </v-btn>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="12">
        <div>Would you like to print or e-mail the patient summary?</div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"></v-col>
      <v-col cols="3">
        <v-btn block color="accent">
          Print
        </v-btn>
      </v-col>
      <v-col cols="3">
        <v-btn block color="accent">
          E-mail
        </v-btn>
      </v-col>
      <v-col cols="2"></v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'PatientDischarge',
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
