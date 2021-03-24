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
          <v-icon x-large color="#1E88E5">mdi-square</v-icon>
          <v-icon x-large color="#E1F5FE">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has not received any doses of the vaccine</div>
        </v-col>
        <v-col cols="2">
          <v-icon x-large color="#FF8F00">mdi-square</v-icon>
          <v-icon x-large color="#FFF8E1">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has received one (1) dose of a vaccine</div>
        </v-col>
        <v-col cols="2">
          <v-icon x-large color="#C2185B">mdi-square</v-icon>
          <v-icon x-large color="#FCE4EC">mdi-square</v-icon>
        </v-col>
        <v-col cols="10">
          <div class="secondary--text">Patient has received two (2) or more doses of the vaccine</div>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>

export default {
  name: "ApplicationInfoPage",
  methods: {},
  components: {},
  data() {
    return {}
  },
  computed: {},
};
</script>