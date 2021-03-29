<template>
  <v-card color="white" elevation="0" tile>
    <v-toolbar color="secondary">
      <v-toolbar-title class="font-weight-medium white--text">Application Information</v-toolbar-title>
    </v-toolbar>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-divider></v-divider>
        </v-col>
      </v-row>
      <v-row align="center"> 
        <v-col cols="12">
          <h3 class="font-weight-medium secondary--text">Page Color-Coding</h3>
        </v-col>
        <v-col cols="2">
          <v-icon x-large :color="zeroDosePrimaryColor">mdi-square</v-icon>
          <v-icon x-large :color="zeroDosePageColor">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has not received any doses of the vaccine / patient is here to receive the first dose.</div>
        </v-col>
        <v-col cols="2">
          <v-icon x-large :color="oneDosePrimaryColor">mdi-square</v-icon>
          <v-icon x-large :color="oneDosePageColor">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has received one (1) dose of a vaccine / patient is here to receive the second dose.</div>
        </v-col>
        <v-col cols="2">
          <v-icon x-large :color="twoDosePrimaryColor">mdi-square</v-icon>
          <v-icon x-large :color="twoDosePageColor">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has received two (2) or more doses of the vaccine / patient is here to receive booster dose.</div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import colorScheme from "../assets/colorScheme";

export default {
  name: "ApplicationInfoPage",
  methods: {},
  components: {},
  data() {
    return {
    }
  },
  computed: {
    zeroDosePrimaryColor() {
      return colorScheme.zeroDosePrimary; 
    }, 
    zeroDosePageColor(){
      return colorScheme.zeroDosePage;
    },
    oneDosePrimaryColor() {
      return colorScheme.oneDosePrimary;
    },
    oneDosePageColor() {
      return colorScheme.oneDosePage;
    },
    twoDosePrimaryColor() {
      return colorScheme.twoDosePrimary;
    },
    twoDosePageColor() {
      return colorScheme.twoDosePage;
    }
  },
};
</script>