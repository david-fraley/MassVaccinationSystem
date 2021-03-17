<template>
  <v-container>
    <v-row>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Lot Number
            </div>
          </v-col>
          <v-col cols="8">
            <v-text-field
              outlined
              dense
              required
              :rules="[(v) => !!v || 'Lot Number field is required']"
              v-model="lotNumber"
              :filled="isVaccinationEventPageReadOnly"
              :disabled="isVaccinationEventPageReadOnly"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Expiration Date
            </div>
          </v-col>
          <v-col cols="8">
            <v-text-field
              outlined
              dense
              required
              :rules="[(v) => !!v || 'Expiration Date field is required']"
              v-model="expirationDate"
              :filled="isVaccinationEventPageReadOnly"
              :disabled="isVaccinationEventPageReadOnly"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Manufacturer
            </div>
          </v-col>
          <v-col cols="8">
            <v-text-field
              outlined
              dense
              required
              :rules="[(v) => !!v || 'Manufacturer field is required']"
              v-model="manufacturer"
              :filled="isVaccinationEventPageReadOnly"
              :disabled="isVaccinationEventPageReadOnly"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
      <!--blank column for spacing-->
      <v-col cols="1"> </v-col>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Dose Quantity
            </div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="doseQuantityOptions"
              outlined
              dense
              required
              :rules="[(v) => !!v || 'Dose Quantity field is required']"
              v-model="doseQuantity"
              :readonly="isVaccinationEventPageReadOnly"
              :filled="isVaccinationEventPageReadOnly"
            ></v-select>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Dose Number
            </div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="doseNumberOptions"
              outlined
              dense
              v-model="doseNumber"
              required
              :rules="[(v) => !!v || 'Dose Number field is required']"
              :readonly="isVaccinationEventPageReadOnly"
              :filled="isVaccinationEventPageReadOnly"
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <p></p>
      <!--blank row for spacing-->
    </v-row>
    <v-row>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">Healthcare Practitioner</div>
          </v-col>
          <v-col cols="8">
            <v-text-field 
              outlined 
              dense 
              filled 
              readonly
              :value="practitionerName"
              :disabled="isVaccinationEventPageReadOnly"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">
              <span style="color:red">*</span>Site of Vaccination
            </div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="vaccinationSiteOptions"
              outlined
              dense
              required
              :rules="[(v) => !!v || 'Site of Vaccination field is required']"
              v-model="site"
              :readonly="isVaccinationEventPageReadOnly"
              :filled="isVaccinationEventPageReadOnly"
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
      <!--blank column for spacing-->
      <v-col cols="1"> </v-col>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">Route</div>
          </v-col>
          <v-col cols="8">
            <v-text-field
              outlined
              dense
              filled
              readonly
              :value="config.route"
              :disabled="isVaccinationEventPageReadOnly"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          placeholder="Notes"
          outlined
          rows="4"
          v-model="note"
          :filled="isVaccinationEventPageReadOnly"
          :disabled="isVaccinationEventPageReadOnly"
        ></v-textarea>
      </v-col>
    </v-row>
    <v-row align="center" justify="center">
      <template>
        <div class="text-center">
          <v-dialog v-model="dialog" width="500">
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
                <v-btn color="primary" text @click="dialog = false">
                  Back
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                  color="primary"
                  text
                  @click="submitVaccinationRecord()"
                >
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
  name: "VaccinationProceedComponent",
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
    encounterID() {
      return this.$store.state.encounterResource.id;
    },
    appointmentID() {
      return this.$store.state.appointmentResource.id;
    },
    isVaccinationEventPageReadOnly() {
      return this.$store.getters.isVaccinationEventPageReadOnly
    },
  },
  methods: {
    onVaccination(immunization) {
      //send data to Vuex
      this.$store.dispatch("vaccinationComplete", immunization);

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
        manufacturer: this.manufacturer,
        lotNumber: this.lotNumber,
        expirationDate: this.expirationDate,
        patient: this.patient,
        encounter: this.encounter,
        status: this.status,
        location: this.location,
        site: this.site,
        route: this.config.route,
        doseQuantity: this.doseQuantity,
        performer: this.practitioner,
        note: this.note,
        education: this.config.education,
        series: this.config.series,
        doseNumber: this.doseNumber,
        seriesDoses: this.config.seriesDoses,
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
      doseNumberOptions: [1, 2],
      doseQuantityOptions: ["0.1 mL", "0.2 mL", "0.5 mL", "1.0 mL"],
      vaccinationSiteOptions: ["Left arm", "Right arm"],
      status: "completed",
      doseQuantity: this.$store.state.immunizationResource.doseQuantity,
      site: this.$store.state.immunizationResource.site,
      lotNumber: this.$store.state.immunizationResource.lotNumber,
      expirationDate: this.$store.state.immunizationResource.expirationDate,
      manufacturer: this.$store.state.immunizationResource.manufacturer,
      note: this.$store.state.immunizationResource.note,
      // Placeholder for patient history
      doseNumber: 1,
    };
  },
};
</script>
