
<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <!-- Last name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Last Name"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>
      <!-- First name -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field label="First Name"></v-text-field>
      </v-col>
    </v-row>

    <v-row align="center" justify="center">
      <!-- Phone Number -->
      <v-col class="d-flex" cols="5" sm="5">
        <v-text-field
          label="Phone Number"
          prepend-icon="mdi-menu-right"
        ></v-text-field>
      </v-col>
      <!-- Phone Number Type -->
      <v-col class="d-flex" cols="5" sm="3">
        <v-select
          v-model="e7"
          :items="phoneType"
          label="Phone Number Type"
        ></v-select>
      </v-col>
      <v-col class="d-flex" cols="5" sm="2"> </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "SinglePatientEmergencyContact",
  data() {
    return {
      phoneType: ["Cell", "Home"],
    };
  },
};
</script>
