<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Location</div>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly outlined
          :value=locationId
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly outlined
          :value=locationName
        ></v-text-field>
      </v-col>
    </v-row>
    
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly outlined
          :value=encounterStatus
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly outlined
          :value=encounterTimeStamp
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
      </v-col>
      <v-col cols="6">
        <v-btn block color="accent" @click="checkInComplete()">
          Patient information verified
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'PatientCheckInComponent',
    computed:
    {
       locationId() {
        return this.$store.state.locationResource.locationId
      },
      locationName() {
        return this.$store.state.locationResource.locationName
      },
      encounterStatus() {
        return this.$store.state.encounterResource.encounterStatus
      },
      encounterTimeStamp() {
        return this.$store.state.encounterResource.encounterTimeStamp
      },
    },
    methods: 
    {
      checkInComplete() {
        //the following is sending dummy data until we have the API in place
        const encounterResourcePayload = {
          encounterStatus:'Arrived',
          encounterTimeStamp: new Date().toISOString(),
        }
        //send data to Vuex
        this.$store.dispatch('patientAdmitted', encounterResourcePayload)
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
