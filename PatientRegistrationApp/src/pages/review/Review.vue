<template>
  <v-container>
    <v-row align="center" justify="start">
      <v-col cols="12">
        <div class="font-weight-medium primary--text">Personal information</div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium">
          Name:
          <span class="font-weight-regular"
            >{{ patient.family }}, {{ patient.given }} {{ patient.middle }}
            {{ patient.suffix }}</span
          >
        </div>
        <div class="font-weight-medium">
          Date of Birth:
          <span class="font-weight-regular">{{ patient.birthDate }}</span>
        </div>
        <div class="font-weight-medium">
          Gender ID:
          <span class="font-weight-regular">{{ patient.gender }}</span>
        </div>
        <div class="font-weight-medium">
          Mother's Maiden Name:
          <span class="font-weight-regular">{{ patient.maiden }}</span>
        </div>
        <div class="font-weight-medium">
          Race:
          <span class="font-weight-regular">{{ patient.race }}</span>
        </div>
        <div class="font-weight-medium">
          Ethnicity:
          <span class="font-weight-regular">{{ patient.ethnicity }}</span>
        </div>
        <div class="font-weight-medium">
          Preferred Language:
          <span class="font-weight-regular">{{ patient.language }}</span>
        </div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium primary--text">Address</div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium">
          Address Type:
          <span class="font-weight-regular">{{ patient.addressUse }}</span>
        </div>
        <div class="font-weight-regular">
          {{ patient.addressLine }}
        </div>
        <template v-if="patient.addressLine2 != ''">
          <div class="font-weight-regular">
            {{ patient.addressLine2 }}
          </div>
        </template>
        <div class="font-weight-regular">
          {{ patient.addressCity }}, {{ patient.addressState }},
          {{ patient.addressCountry }}, {{ patient.addressPostalCode }}
        </div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium primary--text">Contact Information</div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium">
          Phone:
          <span v-if="patient.phone" class="font-weight-regular"
            >{{ patient.phone }}
            <span v-if="patient.phoneType"
              >({{ patient.phoneType }})</span
            ></span
          ><span v-else class="font-weight-regular">Not provided</span>
        </div>
        <div class="font-weight-medium">
          E-mail:
          <span v-if="patient.email" class="font-weight-regular">{{
            patient.email
          }}</span
          ><span v-else class="font-weight-regular">Not provided</span>
        </div>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Emergency Contact</div>
      </v-col>
      <v-col cols="12">
        <div class="font-weight-medium">
          Name:
          <span class="font-weight-regular"
            >{{ patient.contactFamily }},
            {{ patient.contactGiven }}
            <span v-if="patient.contactRelationship"
              >(Relationship: {{ patient.contactRelationship }})</span
            >
          </span>
        </div>
        <div class="font-weight-medium">
          Phone:
          <span class="font-weight-regular"
            >{{ patient.contactPhone }}
            <span v-if="patient.contactPhoneType"
              >({{ patient.contactPhoneType }})</span
            ></span
          >
        </div>
      </v-col>
      <v-col cols="12">
        <h3>Attestations</h3>
        <v-form v-model="formValid" ref="form">
          <v-checkbox
            v-model="patient.verifyInfo"
            :rules="[rules.required('You must verify and authorize')]"
          >
            <template #label>
              <span class="red--text"><strong>* </strong></span> I verify that
              the above information is accurate and authorize its use.
            </template>
          </v-checkbox>
          <v-checkbox
            v-model="patient.allowContact"
            :label="contactInfoAcknowledgement"
            :rules="[rules.required('Contact consent required')]"
          >
            <template #label>
              <span class="red--text"><strong>* </strong></span>
              {{ contactInfoAcknowledgement }}
            </template>
          </v-checkbox>
        </v-form>
      </v-col>
    </v-row>
    <!-- SUBMIT BUTTON -->
    <v-row>
      <v-col cols="12">
        <vue-recaptcha
          ref="recaptcha"
          @verify="onVerify"
          @expired="onExpired"
          :sitekey="recaptchaKey"
          :loadRecaptchaScript="true"
        >
        </vue-recaptcha>
      </v-col>
      <v-col cols="12">
        <v-btn clear color="secondary" class="ma-2 white--text" @click="back()">
          <v-icon left large color="white"> mdi-chevron-left </v-icon>
          Back
        </v-btn>
        <v-btn
            color="primary"
            class="ma-2 white--text"
            @click="submitPatientInfo()"
            :disabled="!formValid || submittingPatient || !recaptchaValid"
          >
            <span>Register</span>
            <v-icon right large color="white"> mdi-chevron-right </v-icon>
            <v-progress-circular
              v-if="submittingPatient"
              size="16"
              indeterminate
              color="white"
              class="ml-2"
            ></v-progress-circular>
          </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
import customerSettings from "@/customerSettings";
import brokerRequests from "@/brokerRequests";
import Rules from "@/utils/commonFormValidation";
import VueRecaptcha from "vue-recaptcha";

export default {
  name: "Review",
  components: { VueRecaptcha },
  data() {
    return {
      rules: Rules,
      submittingPatient: false,
      formValid: false,
      contactInfoAcknowledgement: customerSettings.contactInfoAcknowledgement,
      recaptchaKey: process.env.VUE_APP_RECAPTCHA,
      recaptchaValid: false,
    };
  },
  computed: {
    patient() {
      return this.$store.state.patient.patient;
    },
    //archive screening questions page
    /*answers() {
      return this.$store.state.screeningQuestions.answers;
    },*/
  },
  methods: {
    back() {
      this.$router.push("/patient-info");
      //archive screening questions page
      //this.$router.push("/questions");
    },
    updatePatient(field, value) {
      this.$store.commit("patient/updatePatient", { field, value });
    },
    submitPatientInfo() {
      // short circuit if they hit back button and tried to submit again
      if (this.patient.hasSubmitted) {
        this.$router.push("/followup");
      }

      this.submittingPatient = true;
      let data = this.buildPatientPayload();
      brokerRequests.submitRegistration(data).then((response) => {
        if (response.data) {
          this.updatePatient("identifier", response.data.Patient[0]);
          this.$router.push("/followup");
        } else if (response.error) {
          this.submittingPatient = false;
          console.log(response.error);
          alert("Registration not successful");
        }
      });
    },
    buildPatientPayload() {
      let patient = {
        family: this.patient.family,
        given: this.patient.given,
        middle: this.patient.middle,
        suffix: this.patient.suffix,
        gender: this.patient.gender,
        maiden: this.patient.maiden,
        birthDate: this.patient.birthDate,
        race: this.patient.race,
        ethnicity: this.patient.ethnicity,
        language: this.patient.language,
        address: {
          use: this.patient.addressUse,
          line: this.patient.addressLine,
          line2: this.patient.addressLine2,
          city: this.patient.addressCity,
          state: this.patient.addressState,
          postalCode: this.patient.addressPostalCode,
          country: this.patient.addressCountry,
        },
        contact: {
          family: this.patient.contactFamily,
          given: this.patient.contactGiven,
          relationship: this.patient.contactRelationship,
          phone: {
            value: this.patient.contactPhone,
            use: this.patient.contactPhoneType,
          },
        },
      };
      if (this.patient.email) {
        patient.email = this.patient.email;
      }
      if (this.patient.phone) {
        patient.phone = {
          value: this.patient.phone,
          use: this.patient.phoneType,
        };
      }
      return { Patient: [patient] };
    },
    onVerify(response) {
      this.recaptchaValid = true;
    },
    onExpired() {
      this.recaptchaValid = false;
      this.$refs.recaptcha.reset()
    },
  },
  mounted() {
    if (this.patient.hasSubmitted) {
      this.$router.push("/followup");
    }
    if (!this.patient.family) {
      this.$router.push("/");
      //archive screening questions page
      /*if (!this.patient.family || !this.answers.screeningQ1) {
      this.$router.push("/"); */
    }
  },
};
</script>
