<template>
  <v-container>
    <v-form ref="form" v-model="valid">
      <v-row>
        <p> </p> <!--blank row for spacing-->
      </v-row>
      <v-row>
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Lot Number
          </div>
        </v-col>
        <v-col cols="3">
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
        <v-col cols="1"></v-col>
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Dose Quantity
          </div>
        </v-col>
        <v-col cols="3">
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
      </v-row>
      <v-row class="mt-0">
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Manufacturer
          </div>
        </v-col>
        <v-col cols="3">
          <v-select
            :items="manufacturerOptions"
            outlined
            dense
            required
            :rules="[(v) => !!v || 'Manufacturer field is required']"
            v-model="manufacturer"
            :filled="isVaccinationEventPageReadOnly"
            :disabled="isVaccinationEventPageReadOnly"
          ></v-select>
        </v-col>
        <v-col cols="1"></v-col>
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Dose Number
          </div>
        </v-col>
        <v-col cols="3">
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
      <v-row v-if="manufacturer" class="mt-0">
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Expiration Date
          </div>
        </v-col>
        <v-col cols="3">
          <v-text-field
            outlined
            dense
            required
            :rules="rules.expirationRules"
            v-model="expirationDate"
            :filled="isVaccinationEventPageReadOnly"
            :disabled="isVaccinationEventPageReadOnly"
            :placeholder="dateFormat"
            v-mask="dateMask"
          ></v-text-field>
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
      <v-row class="mt-0">
        <v-col cols="2">
          <div class="secondary--text">Healthcare Practitioner</div>
        </v-col>
        <v-col cols="3">
          <v-text-field 
            outlined 
            dense 
            filled 
            readonly
            :value="practitionerName"
            :disabled="isVaccinationEventPageReadOnly"
          ></v-text-field>
        </v-col>
        <v-col cols="1"></v-col>
        <v-col cols="2">
          <div class="secondary--text">Route</div>
        </v-col>
        <v-col cols="3">
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
      <v-row class="mt-0">
        <v-col cols="2">
          <div class="secondary--text">
            <span style="color:red">*</span>Site of Vaccination
          </div>
        </v-col>
        <v-col cols="3">
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
    </v-form>
    <v-row align="center" justify="center">
      <template>
        <div class="text-center">
          <v-col cols="6">
            <v-btn block color="accent" :disabled="isVaccinationEventPageReadOnly" @click.stop="openDialog">
              Submit vaccination record
            </v-btn>
          </v-col>
          <v-dialog v-model="dialog" width="500">
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
import Rules from "@/utils/commonFormValidation";

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
    dateMask() {
      let mask = '##/##/####';
      if (this.manufacturer === "Pfizer-BioNTech") {
        mask = '##/####'
      }
      return mask;
    },
    dateFormat() {
      let format = "MM/DD/YYYY";
      if (this.manufacturer === "Pfizer-BioNTech") {
        format = "MM/YYYY";
      }
      return format;
    }
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
    openDialog() {
      this.$refs.form.validate();
      if (this.valid) this.dialog = true;
    },
    submitVaccinationRecord() {
      let data = {
        vaccine: this.config.vaccine,
        manufacturer: "Organization/example",
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
      rules: Rules,
      valid: false,
      dialog: false,
      doseNumberOptions: [1, 2],
      doseQuantityOptions: ["0.1 mL", "0.2 mL", "0.5 mL", "1.0 mL"],
      vaccinationSiteOptions: ["Left arm", "Right arm"],
      manufacturerOptions: ["Pfizer-BioNTech", "Moderna", "Johnson & Johnson"],
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
