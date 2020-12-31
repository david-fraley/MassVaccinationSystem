<template>
  <v-app>
    
    <v-app-bar app clipped-right flat height="69" color="primary">
      <h1 class="font-weight-medium white--text">Point of Dispensing Application</h1>
      <v-spacer></v-spacer>
      <img
        src="./assets/logo.png"
        height="100%">
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app width="20em">
      <v-sheet color="white" class="pa-4">
        <v-btn icon color="accent">
              <v-icon large>mdi-account-circle</v-icon>
        </v-btn>
        Welcome, 
      </v-sheet>

      <v-divider></v-divider>

      <v-list>
        <v-list-item-group v-model="leftMenu">
          <!-- Retrieve Patient Record -->
          <v-list-item :disabled="false" router :to="'/RetrievePatientRecord'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-binoculars</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Retrieve Patient Record</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Check-In -->
          <v-list-item :disabled="isCheckInPageDisabled" router :to="'/CheckIn'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-card-account-details-outline</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Check-In</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Patient History -->
          <v-list-item :disabled="isPatientHistoryPageDisabled" router :to="'/PatientHistory'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-history</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Patient History</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Vaccination Event -->
          <v-list-item :disabled="isVaccinationEventPageDisabled" router :to="'/VaccinationEvent'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-medical-bag</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Vaccination Event</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Adverse Reaction -->
          <v-list-item :disabled="isAdverseReactionPageDisabled" router :to="'/AdverseReaction'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-alert</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Adverse Reaction</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Discharge -->
          <v-list-item :disabled="isDischargePageDisabled" router :to="'/Discharge'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-checkbox-marked-outline</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Discharge</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Configuration -->
          <v-list-item :disabled="isConfigurationPageDisabled" router :to="'/Configuration'">
            <v-list-item-action>
              <v-icon large color="secondary">mdi-cog</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">Configuration</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
  export default {
    name: 'App',
    methods: 
    {
    },
    computed:
    {
      isCheckInPageDisabled() {
        return this.$store.getters.isCheckInPageDisabled
      },
      isPatientHistoryPageDisabled() {
        return this.$store.getters.isPatientHistoryPageDisabled
      },
      isVaccinationEventPageDisabled() {
        return this.$store.getters.isVaccinationEventPageDisabled
      },
      isAdverseReactionPageDisabled() {
        return this.$store.getters.isAdverseReactionPageDisabled
      },
      isDischargePageDisabled() {
        return this.$store.getters.isDischargePageDisabled
      },
      isConfigurationPageDisabled() {
        return this.$store.getters.isConfigurationPageDisabled
      },
      workflowState() {
        return this.$store.getters.workflowState
      }
    },
    components: 
    {    
    },
    data () {
      return {
        drawer: null,
        leftMenu: 1
      }
    }
  }
</script>
