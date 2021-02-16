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
            <v-text-field 
              outlined 
              dense 
              filled 
              readonly
              :value="practitionerName"
              :disabled="isVaccinationEventPageReadOnly"
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
              v-model="reason"
              :readonly="isVaccinationEventPageReadOnly"
              :filled="isVaccinationEventPageReadOnly"
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
          v-model="note"
          :readonly="isVaccinationEventPageReadOnly"
          :filled="isVaccinationEventPageReadOnly"
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
                <v-btn block color="accent" v-bind="attrs" v-on="on" :disabled="isVaccinationEventPageReadOnly">
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
  name: "VaccinationCanceledComponent",
  computed: {
    practitioner() {
      return `Practitioner/${this.$store.state.practitionerResource.id}`;
    },
    practitionerName() {
      let practitioner = this.$store.state.practitionerResource;
      return `${practitioner.family}, ${practitioner.given}`;
    },
    patient() {
      return `Patient/${this.$store.state.patientResource.id}`;
    },
    encounter() {
      return `Encounter/${this.$store.state.encounterResource.id}`;
    },
    location() {
      return `Location/${this.$store.state.locationResource.id}`;
    },
    config() {
      return this.$store.state.config;
    },
    isVaccinationEventPageReadOnly() {
        return this.$store.getters.isVaccinationEventPageReadOnly
    },
    encounterID() {
      return this.$store.state.encounterResource.id;
    },
    appointmentID() {
      return this.$store.state.appointmentResource.id;
    },
  },
  methods: {
    onVaccination(immunization) {
      //send data to Vuex
      this.$store.dispatch("vaccinationCanceled", immunization);

      //Advance to the Discharge page
      this.$router.push("Discharge");

      //Close the dialog
      this.dialog = false;

      //end the encounter
      this.endEncounter()
    },
    onDischarge(payload) {
      this.$store.dispatch("patientDischarged", payload);
    },
    submitVaccinationRecord() {
      let data = {
        vaccine: this.config.vaccine,
        patient: this.patient,
        encounter: this.encounter,
        status: this.status,
        reason: this.reason,
        location: this.location,
        performer: this.practitioner,
        note: this.note,
      };
      brokerRequests.submitVaccination(data).then((response) => {
        if (response.data) {
          this.onVaccination(response.data);
        } else if (response.error) {
          console.log(response.error);
          alert("Vaccination record not submitted");
        }
      });
    },
    endEncounter() {
      let data = {
        apptID: this.appointmentID,
        encounterID: this.encounterID,
      };
      brokerRequests.discharge(data).then((response) => {
        if (response.data) {
          this.onDischarge(response.data);
        } else if (response.error) {
          console.log(response.error);
          alert(`Patient not discharged`);
        }
      });
    },
  },
  components: {},
  data() {
    return {
      dialog: false,
      vaccineNotAdministeredOptions: [
        "Medical Precaution",
        "Immunity",
        "Out of Stock",
        "Patient Objection",
      ],
      status: "not-done",
      reason: this.$store.state.immunizationResource.reason,
      note: this.$store.state.immunizationResource.note,
    };
  },
};
</script>
