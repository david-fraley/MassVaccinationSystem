<template>
  <v-app>
    <SystemBar />
    
    <v-app-bar app clipped-right flat height="69" color="primary">
      <h1 class="font-weight-medium white--text">Point of Dispensing Application</h1>
      <v-spacer></v-spacer>
      <img
        src="./assets/logo.png"
        height="100%">
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" app width="20em" permanent>
      <v-sheet color="white" class="pa-4">
        <v-btn icon color="accent">
              <v-icon large>mdi-account-circle</v-icon>
        </v-btn>
        Welcome, 
      </v-sheet>

      <v-divider></v-divider>

      <v-list>
        <v-list-item-group v-model="leftMenu" color="white">
          <!-- Retrieve Patient Record -->
          <v-list-item :disabled="false" router :to="'/RetrievePatientRecord'">
            <v-list-item-action>
              <v-icon large>mdi-binoculars</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Retrieve Patient Record</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Check-In -->
          <v-list-item :disabled="isCheckInPageDisabled" router :to="'/CheckIn'">
            <v-list-item-action>
              <v-icon large>mdi-card-account-details-outline</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Check-In</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Patient History -->
          <v-list-item :disabled="isPatientHistoryPageDisabled" router :to="'/PatientHistory'">
            <v-list-item-action>
              <v-icon large>mdi-history</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Patient History</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Consent and Screening -->
          <v-list-item :disabled="isConsentScreeningPageDisabled" router :to="'/ConsentScreening'">
            <v-list-item-action>
              <v-icon large>mdi-account-check</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Consent and Screening</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Vaccination Event -->
          <v-list-item :disabled="isVaccinationEventPageDisabled" router :to="'/VaccinationEvent'">
            <v-list-item-action>
              <v-icon large>mdi-medical-bag</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Vaccination Event</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- Discharge -->
          <v-list-item :disabled="isDischargePageDisabled" router :to="'/Discharge'">
            <v-list-item-action>
              <v-icon large>mdi-checkbox-marked-outline</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Discharge</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <!-- SAVE FOR FUTURE:  Adverse Reaction>
          <v-list-item :disabled="isAdverseReactionPageDisabled" router :to="'/AdverseReaction'">
            <v-list-item-action>
              <v-icon large>mdi-alert</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Adverse Reaction</v-list-item-title>
            </v-list-item-content>
          </v-list-item-->
          <!-- SAVE FOR FUTURE:  Configuration>
          <v-list-item :disabled="isConfigurationPageDisabled" router :to="'/Configuration'">
            <v-list-item-action>
              <v-icon large>mdi-cog</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium">Configuration</v-list-item-title>
            </v-list-item-content>
          </v-list-item-->
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
  import SystemBar from "@/pages/application/partials/SystemBar";
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
      isConsentScreeningPageDisabled() {
        return this.$store.getters.isConsentScreeningPageDisabled
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
      SystemBar,
    },
    data () {
      return {
        drawer: null,
        leftMenu: 1
      }
    }
  }
</script>
<style scoped>
  .v-list-item:hover{
    background: #BBDEFB;
    color: #424242;
  }
  .v-list-item--disabled {
    background: #F5F5F5;
  }
  .v-list-item--active {
    background: #1565C0;
  }
  .v-system-bar {
    font-size: 2rem;
  }
</style>
