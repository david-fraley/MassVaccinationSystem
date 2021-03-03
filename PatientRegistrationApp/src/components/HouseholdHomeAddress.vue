<template>
  <v-form ref="form" v-model="valid">
    <v-container fluid>
      <v-row align="center" justify="start">
        <!--Address Type-->
        <v-col cols="12" sm="6" md="6" lg="4">
          <v-select
            required
            :rules="[(v) => !!v || 'Address Type is required']"
            v-model="addressType"
            :items="addressTypeOptions"
            prepend-icon="mdi-blank"
          >
            <template #label>
              <span class="red--text"><strong>* </strong></span>Address Type
            </template>
          </v-select>
        </v-col>
      </v-row>
      <v-row align="center" justify="start">
        <!--Address Line 1-->
        <v-col cols="12" sm="12" md="12" lg="6">
          <v-text-field
            required
            :rules="[(v) => !!v || 'Address field is required']"
            v-model="lineAddress1"
            prepend-icon="mdi-blank"
            autocomplete="address-line1"
          >
            <template #label>
              <span class="red--text"><strong>* </strong></span>Address Line 1
            </template>
          </v-text-field>
        </v-col>
        <!--Address Line 2-->
        <v-col cols="12" sm="12" md="12" lg="5">
          <v-text-field
            v-model="lineAddress2"
            prepend-icon="mdi-blank"
            label="Address Line 2"
            autocomplete="address-line2"
          >
          </v-text-field>
        </v-col>
      </v-row>
      <v-row align="center" justify="start">
        <!--City-->
        <v-col cols="12" sm="6" md="6" lg="4">
          <v-text-field
            required
            :rules="[(v) => !!v || 'City field is required']"
            v-model="cityAddress"
            prepend-icon="mdi-blank"
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
            v-model="stateAddress"
            :items="stateOptions"
            prepend-icon="mdi-blank"
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
            v-model="countryAddress"
            :items="countryOptions"
            prepend-icon="mdi-blank"
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
            v-model="postalCode"
            prepend-icon="mdi-blank"
            autocomplete="postal-code"
          >
            <template #label>
              <span class="red--text"><strong>* </strong></span>Zip Code
            </template>
          </v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
import EventBus from "../eventBus";

export default {
  data() {
    return {
      postalCodeRules: [
        (v) => !!v || "Zip code is required",
        (v) =>
          /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) ||
          "Zip code must be in format of ##### or #####-####",
      ],

      stateOptions: [
        "AL",
        "AK",
        "AS",
        "AZ",
        "AR",
        "CA",
        "CO",
        "CT",
        "DE",
        "DC",
        "FM",
        "FL",
        "GA",
        "GU",
        "HI",
        "ID",
        "IL",
        "IN",
        "IA",
        "KS",
        "KY",
        "LA",
        "ME",
        "MH",
        "MD",
        "MA",
        "MI",
        "MN",
        "MS",
        "MO",
        "MT",
        "NE",
        "NV",
        "NH",
        "NJ",
        "NM",
        "NY",
        "NC",
        "ND",
        "MP",
        "OH",
        "OK",
        "OR",
        "PW",
        "PA",
        "PR",
        "RI",
        "SC",
        "SD",
        "TN",
        "TX",
        "UT",
        "VT",
        "VI",
        "VA",
        "WA",
        "WV",
        "WI",
        "WY",
      ],
      addressTypeOptions: ["Home", "Temporary"],
      countryOptions: ["USA"],
      addressType: "",
      lineAddress1: "",
      lineAddress2: "",
      cityAddress: "",
      stateAddress: "",
      countryAddress: "USA",
      postalCode: "",
      valid: false,
    };
  },
  methods: {
    sendHouseholdHomeAddressInfoToReviewPage() {
      const householdHomeAddressPayload = {
        addressType: this.addressType,
        lineAddress1: this.lineAddress1,
        lineAddress2: this.lineAddress2,
        cityAddress: this.cityAddress,
        stateAddress: this.stateAddress,
        countryAddress: this.countryAddress,
        postalCode: this.postalCode,
      };
      EventBus.$emit(
        "DATA_HOUSEHOLD_ADDRESS_INFO_PUBLISHED",
        householdHomeAddressPayload
      );
    },
    verifyFormContents() {
      var valid = true;
      var message = "Woops! You need to enter the following field(s):";

      if (this.addressType == "") {
        message += " Address Type";
        valid = false;
      }
      if (this.lineAddress1 == "") {
        if (!valid) {
          message += ",";
        }
        message += " Address";
        valid = false;
      }
      if (this.cityAddress == "") {
        if (!valid) {
          message += ",";
        }
        message += " City";
        valid = false;
      }
      if (this.stateAddress == "") {
        if (!valid) {
          message += ",";
        }
        message += " State";
        valid = false;
      }
      if (this.countryAddress == "") {
        if (!valid) {
          message += ",";
        }
        message += " Country";
        valid = false;
      }
      if (this.postalCode == "") {
        if (!valid) {
          message += ",";
        }
        message += " Zipcode";
        valid = false;
      }
      if (valid == false) {
        alert(message);
        return false;
      }
      this.$refs.form.validate();
      if (!this.valid) return;
      this.sendHouseholdHomeAddressInfoToReviewPage();
      return true;
    },
  },
};
</script>
