<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        Reason vaccine was not administered
      </v-col>
      <v-col cols="4">
        <v-select
          :items="vaccineNotAdministeredOptions"
          outlined
          dense
          required
          :rules="[v => !!v || 'Reason field is required']"
          v-model="ReasonNotAdministered"
        ></v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        Vaccination status
      </v-col>
      <v-col cols="4">
        <v-text-field outlined dense filled readonly
          value="status"
          id="vaccinationStatus"
        ></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-text-field outlined dense filled readonly
          value="time"
          id="vaccinationStatusTime"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2">
        Healthcare practitioner
      </v-col>
      <v-col cols="4">
        <v-text-field outlined dense filled readonly
          value="completed"
          id="healthcarePractitioner"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          id="notes"
          placeholder="Notes"
          outlined
          rows="4"
        ></v-textarea>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent">
          Submit vaccination record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'VaccinationCanceledComponent',
    methods: 
    {
    },
    components: 
    {
    },
    data () {
      return {
        dialog: false,
        vaccineNotAdministeredOptions: ['Medical precaution', 'Immune', 'Out of Stock', 'Patient Objection'],
        ReasonNotAdministered: " "
      }
    }
  }
</script>
