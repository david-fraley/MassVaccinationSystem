<template>
  <v-container>
    <v-row>
      <p> </p> <!--blank row for spacing-->
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-row no-gutters>
          <v-col cols="6">
            <div class="secondary--text">Healthcare Practitioner</div>
          </v-col>
          <v-col cols="6">
            <v-text-field outlined dense filled readonly
              :value=healthcarePractitioner
            ></v-text-field>
          </v-col>
          <v-col cols="6">
            <div class="secondary--text"><span style="color:red">*</span>Reason vaccine was not administered</div>
          </v-col>
          <v-col cols="6">
            <v-select
              :items="vaccineNotAdministeredOptions"
              outlined
              dense
              required
              :rules="[v => !!v || 'Reason vaccine was not administered field is required']"
              v-model="notAdministeredReason"
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
      <!--blank column for spacing-->
      <v-col cols="6">
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          placeholder="Notes"
          outlined
          rows="4"
          :value="notes"
          v-model="notes"
        ></v-textarea>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <template> 
        <div class="text-center">
          <v-dialog
            v-model="dialog"
            width="500"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-col cols="6">
                <v-btn block color="accent" v-bind="attrs" v-on="on">
                  Submit vaccination record
                </v-btn>
              </v-col>
          </template>
            <v-card>
              <v-card-title class="headline grey lighten-2 justify-center"> 
                Are you sure you want to submit?
              </v-card-title>
              <v-card-actions>
                <v-btn	
                  color="primary"
                  text
                  @click="dialog = false">
                  Back
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn	
                  color="primary"
                  text
                  @click="submitVaccinationRecord()">
                  Submit
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </div>
      </template>
    </v-row>
  </v-container>
</template>

<script>
  import brokerRequests from "../brokerRequests";
  
  export default {
    name: 'VaccinationCanceledComponent',
    computed: {
      healthcarePractitioner() {
        return this.$store.state.immunizationResource.healthcarePractitioner
      }
    },
    methods: {
      onSuccess() {
        const vaccinationCanceledPlayload = {
          immunizationStatus: "Not-done",
          immunizationTimeStamp: new Date().toISOString(),
          healthcarePractitioner: this.healthcarePractitioner,
          notAdministeredReason: this.notAdministeredReason,
          notes: this.notes,
        };

        //send data to Vuex
        this.$store.dispatch("vaccinationCanceled", vaccinationCanceledPlayload);

        //Advance to the Discharge page
        this.$router.push("Discharge");

        //Close the dialog
        this.dialog = false;
      },
      submitVaccinationRecord() {
        brokerRequests.submitVaccination().then((response) => {
          if (response.data) {
            this.onSuccess();
          } else if (response.error) {
            alert("Vaccination record not submitted");
          }
        });
      }
    },
    components: {},
  data() {
    return {
      dialog: false,
      vaccineNotAdministeredOptions: [
        "Medical precaution",
        "Immune",
        "Out of Stock",
        "Patient Objection",
      ],
      notAdministeredReason: this.$store.state.immunizationResource
        .notAdministeredReason,
      notes: this.$store.state.immunizationResource.notes,
    };
  },
};
</script>
