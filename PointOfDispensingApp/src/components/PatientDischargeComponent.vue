<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Vaccination status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly
          value="vaccination status"
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly
          value="2020-07-20 14:23:47"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly
          :value=encounterStatus
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly
          :value=encounterTimeStamp
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
      </v-col>    
      <v-col cols="6">
        <v-btn block color="accent" @click="endEncounter()">
          End encounter
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">Would you like to print or e-mail the patient summary?</div>
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
    name: 'PatientDischargeComponent',
    computed:
    {
      encounterStatus() {
        return this.$store.state.encounterResource.encounterStatus
      },
      encounterTimeStamp() {
        return this.$store.state.encounterResource.encounterTimeStamp
      },
    },
    methods: 
    {
      endEncounter() {
        //the following is sending dummy data until we have the API in place
        const encounterResourcePayload = {
          encounterStatus:'Finished',
          encounterTimeStamp: new Date().toISOString(),
        }
        //send data to Vuex
        this.$store.dispatch('patientDischarged', encounterResourcePayload)
      }
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
