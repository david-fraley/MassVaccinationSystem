<template>
  <v-container>
    <v-form ref="form" v-model="valid">
      <v-row align="center" justify="start">
        <v-col cols="12">
          <v-row align="center" justify="start"
            ><v-col cols="12">
              <v-alert
                elevation="2"
                color="info"
                outlined
                :value="showWelcome"
                transition="scroll-y-transition"
              >
                <h2>{{ welcome }}</h2>
                <div><br />{{ introStatement }}</div>
                <div><br />{{ expectationsStatement }}</div>
                <div><br />{{ finalStatement }}</div>
                <div class="pt-2 float-right">
                  <v-btn @click="showWelcome = false" color="blue lighten-4"
                    >Close</v-btn
                  >
                </div>
              </v-alert>
            </v-col>
          </v-row>
          <!-- PERSONAL INFO -->
          <v-row class="py-2">
            <v-col cols="12">
              <h2>Personal Info</h2>
            </v-col>
            <!-- First name -->
            <v-col cols="12" sm="6" md="6" lg="3">
              <v-text-field
                required
                :rules="[(v) => !!v || 'First name field is required']"
                :value="patient.given"
                @input="updatePatient('given', $event)"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>First Name
                </template>
              </v-text-field>
            </v-col>
            <!-- Middle name -->
            <v-col cols="12" sm="6" md="6" lg="3">
              <v-text-field
                :value="patient.middle"
                @input="updatePatient('middle', $event)"
                label="Middle Name"
              >
              </v-text-field>
            </v-col>
            <!-- Last name -->
            <v-col cols="12" sm="6" md="6" lg="3">
              <v-text-field
                required
                :rules="[(v) => !!v || 'Last name field is required']"
                :value="patient.family"
                @input="updatePatient('family', $event)"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Last Name
                </template>
              </v-text-field>
            </v-col>
            <!-- Suffix -->
            <v-col cols="12" sm="6" md="6" lg="3">
              <v-select
                label="Suffix"
                :items="suffixOptions"
                :value="patient.suffix"
                @input="updatePatient('suffix', $event)"
              >
              </v-select>
            </v-col>
            <v-col cols="12" sm="6" md="6" lg="4">
              <!-- Date of Birth -->
              <v-text-field
                :value="patient.birthDate"
                @input="updatePatient('birthDate', $event)"
                :rules="birthdateRules"
                placeholder="MM/DD/YYYY"
                v-mask="'##/##/####'"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Date of
                  Birth
                </template>
              </v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="6" lg="4">
              <!-- Gender identity -->
              <v-select
                :items="genderIdOptions"
                required
                :rules="[(v) => !!v || 'Gender identity field is required']"
                :value="patient.gender"
                @input="updatePatient('gender', $event)"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Gender
                  Identity
                </template>
              </v-select>
            </v-col>
            <v-col cols="12" sm="6" md="6" lg="4">
              <!-- Race -->
              <v-select
                :value="patient.race"
                @input="updatePatient('race', $event)"
                :items="raceOptions"
                required
                :rules="[(v) => !!v || 'Race is required']"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Race
                </template>
                ></v-select
              >
            </v-col>
            <v-col cols="12" sm="6" md="6" lg="4">
              <!-- Ethnicity -->
              <v-select
                :value="patient.ethnicity"
                @input="updatePatient('ethnicity', $event)"
                :items="ethnicityOptions"
                required
                :rules="[(v) => !!v || 'Ethnicity is required']"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Ethnicity
                </template>
              </v-select>
            </v-col>
            <!-- Language -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-select
                required
                :items="preferredLanguageOptions"
                :rules="[(v) => !!v || 'Preferred language field is required']"
                :value="patient.language"
                @input="updatePatient('language', $event)"
                ><template #label>
                  <span class="red--text"><strong>* </strong></span>Language
                  During Vaccination
                </template></v-select
              >
            </v-col>
            <!-- Current Photo -->
            <!-- the "rules" checks that the image size is less than 2 MB -->
            <!-- <v-col cols="12" sm="6" md="6" lg="4">
              <v-file-input
                accept="image/png, image/jpeg, image/bmp"
                :rules="[rules.photoSize]"
                v-model="patientPhoto"
                label="Photo"
                prepend-icon="mdi-camera"
              ></v-file-input>
            </v-col> -->
          </v-row>
          <!-- ADDRESS -->
          <v-row class="py-2">
            <v-col cols="12"><h2>Address</h2></v-col>
            <!--Address Type-->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-select
                required
                :rules="[(v) => !!v || 'Address Type is required']"
                :value="patient.addressUse"
                @input="updatePatient('addressUse', $event)"
                :items="addressTypeOptions"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Address Type
                </template>
              </v-select>
            </v-col>
          </v-row>
          <v-row>
            <!--Address Line 1-->
            <v-col cols="12" sm="12" md="12" lg="6">
              <v-text-field
                required
                :rules="[(v) => !!v || 'Address field is required']"
                :value="patient.addressLine"
                @input="updatePatient('addressLine', $event)"
                autocomplete="address-line1"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Address Line
                  1
                </template>
              </v-text-field>
            </v-col>
            <!--Address Line 2-->
            <v-col cols="12" sm="12" md="12" lg="5">
              <v-text-field
                :value="patient.addressLine2"
                @input="updatePatient('addressLine2', $event)"
                label="Address Line 2"
                autocomplete="address-line2"
              >
              </v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <!--City-->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                required
                :rules="[(v) => !!v || 'City field is required']"
                :value="patient.addressCity"
                @input="updatePatient('addressCity', $event)"
                autocomplete="address-level2"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>City
                </template>
              </v-text-field>
            </v-col>
            <!--State-->
            <v-col cols="12" sm="6" md="6" lg="2">
              <v-select
                required
                :rules="[(v) => !!v || 'State field is required']"
                :value="patient.addressState"
                @input="updatePatient('addressState', $event)"
                :items="stateOptions"
                autocomplete="address-level1"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>State
                </template>
              </v-select>
            </v-col>
            <!--Country-->
            <v-col cols="12" sm="6" md="6" lg="2">
              <v-select
                required
                :rules="[(v) => !!v || 'Country field is required']"
                :value="patient.addressCountry"
                @input="updatePatient('addressCountry', $event)"
                :items="countryOptions"
                autocomplete="country"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Country
                </template>
              </v-select>
            </v-col>
            <!--Zip Code-->
            <v-col cols="12" sm="6" md="6" lg="3">
              <v-text-field
                required
                :rules="postalCodeRules"
                :value="patient.addressPostalCode"
                @input="updatePatient('addressPostalCode', $event)"
                autocomplete="postal-code"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Zip Code
                </template>
              </v-text-field>
            </v-col>
          </v-row>
          <!-- PHONE -->
          <v-row class="py-2">
            <v-col cols="12"><h2>Personal Contact</h2></v-col>
          </v-row>
          <v-row>
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                :value="patient.phone"
                @input="updatePatient('phone', $event)"
                :rules="phoneNumberRulesUs"
                v-mask="'(###)###-####'"
                prepend-icon="mdi-phone"
              >
                <template #label
                  ><span class="red--text"><strong>* </strong></span>Phone
                  Number</template
                >
              </v-text-field>
            </v-col>
            <v-col cols="11" sm="5" md="5" lg="2">
              <v-select
                :value="patient.phoneType"
                @input="updatePatient('phoneType', $event)"
                :items="phoneTypeOptions"
                :rules="[(v) => !!v || 'Required']"
                ><template #label
                  ><span class="red--text"><strong>* </strong></span>Phone
                  Type</template
                ></v-select
              >
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                :value="patient.email"
                @input="updatePatient('email', $event)"
                :rules="emailRules"
                prepend-icon="mdi-email"
              >
                <template #label
                  ><span class="red--text"><strong>* </strong></span>E-mail
                  Address</template
                >
              </v-text-field>
            </v-col>
          </v-row>
          <!--EMERGENCY CONTACT -->
          <v-row class="py-2">
            <v-col cols="12">
              <h2>Emergency Contact</h2>
            </v-col>
            <!-- First name -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                required
                :rules="[(v) => !!v || 'First name field is required']"
                :value="patient.contactGiven"
                @input="updatePatient('contactGiven', $event)"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Contact
                  First Name
                </template>
              </v-text-field>
            </v-col>
            <!-- Last name -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                required
                :rules="[(v) => !!v || 'Last name field is required']"
                :value="patient.contactFamily"
                @input="updatePatient('contactFamily', $event)"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Contact Last
                  Name
                </template>
              </v-text-field>
            </v-col>
          </v-row>
          <v-row align="center" justify="start">
            <!-- Phone Number -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-text-field
                required
                :rules="phoneNumberRulesUs"
                v-mask="'(###)###-####'"
                :value="patient.contactPhone"
                @input="updatePatient('contactPhone', $event)"
                prepend-icon="mdi-phone"
              >
                <template #label>
                  <span class="red--text"><strong>* </strong></span>Contact
                  Phone Number
                </template>
              </v-text-field>
            </v-col>
            <!-- Phone Number Type -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-select
                :value="patient.contactPhoneType"
                @input="updatePatient('contactPhoneType', $event)"
                :items="phoneTypeOptions"
                :rules="[(v) => !!v || 'Required']"
                ><template #label
                  ><span class="red--text"><strong>* </strong></span>Contact
                  Phone Type</template
                ></v-select
              >
            </v-col>
          </v-row>
          <v-row align="center" justify="start">
            <!-- Relationship Type -->
            <v-col cols="12" sm="6" md="6" lg="4">
              <v-select
                :value="patient.contactRelationship"
                @input="updatePatient('contactRelationship', $event)"
                :items="relationshipOptions"
                label="Relationship: this person is your"
              >
              </v-select>
            </v-col>
          </v-row>
          <!-- SUBMIT BUTTON -->
          <v-row>
            <v-col cols="12" sm="12" md="6">
              <v-alert
                dense
                outlined
                dismissible
                type="error"
                v-model="hasValidationErrors"
              >
                There are errors in the form, please fill out form completely.
              </v-alert>
            </v-col>
            <v-col cols="12">
              <v-btn
                color="primary"
                class="ma-2 white--text"
                @click="validateAndSubmit()"
              >
                Continue
                <v-icon right large color="white"> mdi-chevron-right </v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
import customerSettings from "@/customerSettings";
import Rules from "@/utils/commonFormValidation";
import UsStatesList from "@/utils/usStates";

export default {
  name: "SinglePatientContactInfo",
  data() {
    return {
      rules: Rules,
      stateOptions: UsStatesList,
      preferredLanguageOptions: ["English", "Spanish"],
      suffixOptions: ["II", "III", "IV", "Jr", "Sr"],
      genderIdOptions: ["Male", "Female", "Other", "Decline to answer"],
      raceOptions: [
        "Black or African American",
        "White",
        "Asian",
        "American Indian or Alaska Native",
        "Native Hawaiian or Other Pacific Islander",
        "Other",
      ],
      ethnicityOptions: [
        "Hispanic or Latino",
        "Not Hispanic or Latino",
        "Unknown or prefer not to answer",
      ],
      addressTypeOptions: ["Home", "Temporary"],
      countryOptions: ["USA"],
      relationshipOptions: [
        "Spouse",
        "Parent",
        "Guardian",
        "Care Giver",
        "Sibling",
        "Grandparent",
        "Child",
        "Foster Child",
        "Stepchild",
        "Other",
      ],
      minDateStr: "1900-01-01",
      birthdateRules: [
        (v) => !!v || "DOB is required",
        // check if v exists before seeing if the length is 10
        (v) =>
          (!!v && v.length === 10) ||
          "DOB must be in specified format, MM/DD/YYYY",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
      phoneTypeOptions: ["Home", "Mobile", "Work"],
      emailRules: [
        (v) =>
          /^[\s]*$|.+@.+\..+/.test(v) ||
          "Please provide a valid e-mail address",
        (v) => !!v || "Required",
      ],
      phoneNumberRulesUs: [
        (v) => {
          return v.length == 13 || "Phone number must be 10 digits";
        },
      ],
      postalCodeRules: [
        (v) => !!v || "Zip code is required",
        (v) =>
          /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) ||
          "Zip code must be in format of ##### or #####-####",
      ],
      householdDefinition: customerSettings.householdDefinition,
      welcome: customerSettings.welcome,
      introStatement: customerSettings.introStatement,
      expectationsStatement: customerSettings.expectationsStatement,
      finalStatement: customerSettings.finalStatement,
      disclosureStatement: customerSettings.contactInfoDisclosure,
      consequenceStatement: customerSettings.contactInfoConsequence,
      acknowledgementStatement: customerSettings.contactInfoAcknowledgement,
      valid: false,
      hasValidationErrors: false,
      showWelcome: true,
    };
  },
  computed: {
    maxDateStr: function() {
      let d = new Date();
      let date = [
        d.getFullYear(),
        ("0" + (d.getMonth() + 1)).slice(-2),
        ("0" + d.getDate()).slice(-2),
      ].join("-");

      return date;
    },
    patient() {
      return this.$store.state.patient.patient;
    },
  },
  methods: {
    updatePatient(field, value) {
      this.$store.commit("patient/updatePatient", { field, value });
    },
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var formattedDate = this.parseDate(birthdate);
      var date = Date.parse(formattedDate);

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    parseDate(date) {
      if (!date) return null;
      // Ensure birthdate is fully entered and can be converted into 3 variables before parsing
      if (date.split("/").length !== 3) return null;

      const [month, day, year] = date.split("/");
      return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
    },
    validateAndSubmit() {
      this.$refs.form.validate();
      if (!this.valid) {
        this.hasValidationErrors = true;
        return;
      }

      this.hasValidationErrors = false;
      this.$router.push("questions");
    },
  },
  mounted() {
    if (this.patient.hasSubmitted) {
      this.$store.commit("patient/resetPatient");
    }
  },
};
</script>

<style></style>
