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
        <!-- v-list-tile is changed to v-list-item -->
        <v-list-item-group v-model="leftMenu">
          <v-list-item v-for="link in links" :key="link.text" router :to="link.route" :disabled="link.disabled">
            <v-list-item-action>
              <v-icon large color="secondary">{{ link.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title class="font-weight-medium secondary--text">{{ link.text }}</v-list-item-title>
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
      isDisabled() {
        return !this.$store.isCheckInPageDisabled
      }
    },
    components: 
    {    
    },
    data () {
      return {
        drawer: null,
        links: [
          { icon: 'mdi-binoculars', text: 'Retrieve Patient Record', route: '/RetrievePatientRecord', disabled: false},
          { icon: 'mdi-card-account-details-outline', text: 'Check-In', route: '/CheckIn', disabled: !this.$store.isCheckInPageDisabled},
          { icon: 'mdi-history', text: 'Patient History', route: '/PatientHistory', disabled: !this.$store.isPatientHistoryPageDisabled},
          { icon: 'mdi-medical-bag', text: 'Vaccination Event', route: '/VaccinationEvent', disabled: !this.$store.isVaccinationEventPageDisabled},
          { icon: 'mdi-alert', text: 'Adverse Reaction', route: '/AdverseReaction', disabled: !this.$store.isAdverseReactionPageDisabled},
          { icon: 'mdi-checkbox-marked-outline', text: 'Discharge', route: '/Discharge', disabled: !this.$store.isDischargePageDisabled},
          { icon: 'mdi-cog', text: 'Configuration', route: '/LocationResource', disabled: !this.$store.isConfigurationPageDisabled}
       ],
       workflowState: this.$store.state.workflowState,
       leftMenu: 1
      }
    }
  }
</script>
