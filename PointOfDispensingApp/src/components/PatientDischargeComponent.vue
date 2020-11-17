<template>
  <v-container>
    <v-row>
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Vaccination status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          value="vaccination status"
        ></v-text-field>
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
      <v-col cols="2">
        <div class="font-weight-medium secondary--text">Encounter status</div>
      </v-col>
      <v-col cols="3">
        <v-text-field
          filled
          dense
          readonly
          value="encounter status"
        ></v-text-field>
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
        <v-btn block color="accent"> End encounter </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">
          Would you like to print or e-mail the patient summary?
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"></v-col>
      <v-col cols="3">
        <v-btn block color="accent"> Print </v-btn>
      </v-col>
      <v-col cols="3">
        <v-btn block color="accent"> E-mail </v-btn>
      </v-col>
      <v-col cols="2"></v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "PatientDischargeComponent",
  methods: {},
  components: {},
  data() {
    return {};
  },
};
</script>
