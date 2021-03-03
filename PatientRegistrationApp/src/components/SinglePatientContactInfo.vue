<template>
  <v-form ref="form" v-model="valid">
    <v-container fluid>
      <v-row align="center" justify="start">
        <v-col cols="12">
          <div>{{ disclosureStatement }}</div>
          <div><br />{{ consequenceStatement }}</div>
          <div>
            <strong
              >We strongly encourage you to provide your e-mail and/or phone
              number as contact information.</strong
            >
          </div>
          <div><br /></div>
          <v-divider></v-divider>
          <div>
            <br />By providing your e-mail and/or phone number as contact
            information, you agree to the following:
          </div>
          <div>
            <strong>{{ acknowledgementStatement }}</strong>
          </div>
        </v-col>
      </v-row>
      <v-row align="center" justify="start" no-gutters>
        <v-checkbox v-model="acknowledgementCheckBox">
          <template #label>
            <span class="red--text"
              ><strong>* <br /></strong></span
            >I have read and understood the above.
          </template>
        </v-checkbox>
      </v-row>
      <v-row align="center" justify="start" no-gutters>
        <v-checkbox
          v-model="permissionCheckBox"
          :disabled="!acknowledgementCheckBox"
          label="I agree to provide and authorize the use of my contact information as outlined above."
        >
        </v-checkbox>
      </v-row>
      <v-divider></v-divider>
      <v-row align="center" justify="start">
        <v-col cols="12" sm="6" md="6" lg="4">
          <v-text-field
            v-model="patientPhoneNumber"
            :disabled="!permissionCheckBox"
            :rules="phoneNumberRulesUs"
            v-mask="'(###)###-####'"
            prepend-icon="mdi-phone"
            label="Phone Number"
          >
          </v-text-field>
        </v-col>
        <v-col cols="11" sm="5" md="5" lg="2">
          <v-select
            v-model="patientPhoneNumberType"
            :disabled="!permissionCheckBox"
            :items="phoneTypeOptions"
            label="Phone Type"
            prepend-icon="mdi-blank"
          ></v-select>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" sm="6" md="6" lg="4">
          <v-text-field
            v-model="patientEmail"
            :disabled="!permissionCheckBox"
            :rules="emailRules"
            prepend-icon="mdi-email"
            label="E-mail Address"
          >
          </v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
import EventBus from "../eventBus";
import customerSettings from "../customerSettings";

export default {
  name: "SinglePatientContactInfo",
  data() {
    return {
      phoneTypeOptions: ["Home", "Mobile", "Work"],
      emailRules: [
        (v) =>
          /^[\s]*$|.+@.+\..+/.test(v) ||
          "Please provide a valid e-mail address",
      ],
      phoneNumberRulesUs: [
        (v) => {
          if (v) return v.length == 13 || "Phone number must be 10 digits";
          else return true;
        },
      ],
      patientPhoneNumber: "",
      patientPhoneNumberType: "",
      patientEmail: "",
      acknowledgementCheckBox: false,
      permissionCheckBox: false,
      disclosureStatement: customerSettings.contactInfoDisclosure,
      consequenceStatement: customerSettings.contactInfoConsequence,
      acknowledgementStatement: customerSettings.contactInfoAcknowledgement,
      valid: false,
    };
  },
  methods: {
    sendContactInfoInfoToReviewPage() {
      const contactInfoPayload = {
        patientPhoneNumber: this.patientPhoneNumber,
        patientPhoneNumberType: this.patientPhoneNumberType,
        patientEmail: this.patientEmail,
      };
      EventBus.$emit("DATA_CONTACT_INFO_PUBLISHED", contactInfoPayload);
    },
    verifyFormContents() {
      var valid = true;
      var message;

      if (this.acknowledgementCheckBox) {
        if (this.permissionCheckBox) {
          if (this.patientPhoneNumber == "" && this.patientEmail == "") {
            message = "Please provide an e-mail address and/or phone number.";
            valid = false;
          }
        }
      } else {
        message = "Acknowledgement is required.";
        valid = false;
      }
      if (valid == false) {
        alert(message);
        return false;
      }
      this.$refs.form.validate();
      if (!this.valid) return;

      this.sendContactInfoInfoToReviewPage();
      return true;
    },
  },
};
</script>
