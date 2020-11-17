<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Location</div>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          value="POD identifier"
        ></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly value="POD name"></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-btn color="accent"> Change location... </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field filled dense readonly value="arrived"></v-text-field>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          value="2020-07-20 14:23:47"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"> </v-col>
      <v-col cols="6">
        <v-btn block color="accent"> Patient information verified </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "PatientCheckInComponent",
  methods: {},
  components: {},
  data() {
    return {};
  },
};
</script>
