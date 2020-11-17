<template>
  <v-app>
    
    <v-app-bar app clipped-right flat height="69" color="primary">
      <h1 class="font-weight-medium white--text">Point of Dispensing Application</h1>
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
        <v-list-item v-for="link in links" :key="link.text" router :to="link.route">
          <v-list-item-action>
            <v-icon large color="secondary">{{ link.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title class="font-weight-medium secondary--text">{{ link.text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
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
import UserLoginPage from './components/UserLoginPage';
import PatientCheckInPage from './components/PatientCheckInPage';
import PatientHistoryPage from './components/PatientHistoryPage';
import VaccinationEventPage from './components/VaccinationEventPage';
import AdverseEventsPage from './components/AdverseEventsPage';
import DischargePage from './components/DischargePage';

  export default {
    name: 'App',
    methods: 
    {
    },
    components: 
    {    
    },
    data () {
      return {
        drawer: null,
        links: [
          { icon: 'mdi-binoculars', text: 'Retrieve Patient Record', route: '/RetrievePatientRecord'},
          { icon: 'mdi-card-account-details-outline', text: 'Check-In', route: '/CheckIn'},
          { icon: 'mdi-history', text: 'Patient History', route: '/PatientHistory'},
          { icon: 'mdi-medical-bag', text: 'Vaccination Event', route: '/VaccinationEvent'},
          { icon: 'mdi-alert', text: 'Adverse Reaction', route: '/AdverseReaction'},
          { icon: 'mdi-checkbox-marked-outline', text: 'Discharge', route: '/Discharge'},
       ]
      }
    }
  }
</script>
