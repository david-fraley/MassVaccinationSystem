<template>
  <v-app>
    
    <v-app-bar app clipped-right flat height="69" color="primary">
      <h2 class="font-weight-medium white--text">Page Title</h2>
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
        <v-list-item
          v-for="[icon, text] in links"
          :key="icon"
          link
        >
          <v-list-item-icon>
            <v-icon large color="secondary">{{ icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title class="font-weight-medium secondary--text">{{ text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container class="py-8 px-6" fluid>
        <v-row>
          <v-col cols="12">
            Guts
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
  export default {
    data: () => ({
      drawer: null,
      links: [
        ['mdi-binoculars', 'Retrieve Patient Record'],
        ['mdi-card-account-details-outline', 'Check-In'],
        ['mdi-history', 'Patient History'],
        ['mdi-medical-bag', 'Vaccination Event'],
        ['mdi-alert', 'Adverse Reaction'],
        ['mdi-checkbox-marked-outline', 'Discharge'],
      ],
    }),
  }
</script>