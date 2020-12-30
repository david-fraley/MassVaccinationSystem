<template>
  <v-container>
    <p>Patient workflow state is: {{ workflowState }}</p>
    <v-row>
      <v-col cols="3">
        <v-img 
          max-height="220" 
          max-width="200"
          src="../assets/blankPicture.png">
        </v-img>
        <div class="font-weight-medium">Patient ID:  <span class="font-weight-regular">{{patientId}}</span></div>
      </v-col>

      <v-col cols="9">
        <v-row no-gutters>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              :value=patientLastName
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              :value=patientFirstName
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              :value=patientDateOfBirth
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Gender</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              :value=patientGender
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Preferred Language</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              :value=patientPreferredLanguage
            ></v-text-field>
          </v-col>
          <v-col cols="6">
            <!--empty columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address</div>
          </v-col>
          <v-col cols="9">
            <v-text-field filled dense readonly outlined 
              :value=patientStreetAddress
            ></v-text-field>
          </v-col>
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
    name: 'PatientInfoComponent',
    computed: {
      workflowState () {
        return this.$store.state.workflowState
      },
      patientId() {
        return this.$store.state.patientResource.patientId
      },
      patientLastName() {
        return this.$store.state.patientResource.patientLastName
      },
      patientFirstName() {
        return this.$store.state.patientResource.patientFirstName
      },
      patientDateOfBirth() {
        return this.$store.state.patientResource.patientDateOfBirth
      },
      patientGender() {
        return this.$store.state.patientResource.patientGender
      },
      patientStreetAddress() {
        return this.$store.state.patientResource.patientStreetAddress
      },
      patientPreferredLanguage() {
        return this.$store.state.patientResource.patientPreferredLanguage
      }
    },
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
