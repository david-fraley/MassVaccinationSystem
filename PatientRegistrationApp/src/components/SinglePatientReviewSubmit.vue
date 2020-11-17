<template>
  <v-container fluid>
    <v-row align="center" justify="center"> </v-row>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Personal information</div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="4" sm="3">
        <v-img
          style="float: left"
          max-height="100"
          max-width="100"
          src="../assets/blankPicture.png"
        >
        </v-img>
      </v-col>
      <v-col cols="8" sm="6">
        <div class="font-weight-medium">
          Name:
          <span class="font-weight-regular"
            >{{ dataPersonalInfo.familyName }},
            {{ dataPersonalInfo.givenName }} {{ dataPersonalInfo.suffix }}</span
          >
        </div>
        <div class="font-weight-medium">
          DOB:
          <span class="font-weight-regular">{{
            dataPersonalInfo.birthDate
          }}</span>
        </div>
        <div class="font-weight-medium">
          Gender ID:
          <span class="font-weight-regular">{{ dataPersonalInfo.gender }}</span>
        </div>
        <div class="font-weight-medium">
          Race(s):
          <span class="font-weight-regular">{{
            dataPersonalInfo.raceSelections
          }}</span>
        </div>
        <div class="font-weight-medium">
          Ethnicity:
          <span class="font-weight-regular">{{
            dataPersonalInfo.ethnicitySelection
          }}</span>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Home Address</div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium">
          Street Address:
          <span class="font-weight-regular">{{
            dataHomeAddress.lineAddress
          }}</span>
        </div>
        <div class="font-weight-medium">
          City, County, State, Country, Zip Code:
          <span class="font-weight-regular"
            >{{ dataHomeAddress.cityAddress }},
            {{ dataHomeAddress.districtAddress }},
            {{ dataHomeAddress.stateAddress }},
            {{ dataHomeAddress.countryAddress }},
            {{ dataHomeAddress.postalCode }}</span
          >
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Contact Information</div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium">
          Phone:
          <span class="font-weight-regular"
            >{{ dataContactInfo.patientPhoneNumber }} ({{
              dataContactInfo.patientPhoneNumberType
            }})</span
          >
        </div>
        <div class="font-weight-medium">
          E-mail:
          <span class="font-weight-regular">{{
            dataContactInfo.patientEmail
          }}</span>
        </div>
        <div class="font-weight-medium">
          Follow-up approval:
          <span class="font-weight-regular">{{
            dataContactInfo.approval
          }}</span>
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Emergency Contact</div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium">
          Name:
          <span class="font-weight-regular"
            >{{ dataEmergencyContact.emergencyContactFamilyName }},
            {{ dataEmergencyContact.emergencyContactGivenName }}</span
          >
        </div>
        <div class="font-weight-medium">
          Phone:
          <span class="font-weight-regular"
            >{{ dataEmergencyContact.emergencyContactPhoneNumber }} ({{
              dataEmergencyContact.emergencyContactPhoneNumberType
            }})</span
          >
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from "../eventBus";

export default {
  data() {
    return {
      dataPersonalInfo: {
        familyName: "",
        givenName: "",
        suffix: "",
        birthDate: "",
        gender: "",
        patientPhoto: "../assets/blankPicture.png",
        raceSelections: "N/A",
        ethnicitySelection: "N/A",
      },
      dataEmergencyContact: {
        emergencyContactFamilyName: "",
        emergencyContactFivenName: "",
        emergencyContactPhoneNumber: "",
        emergencyContactPhoneNumberType: "",
      },
      dataHomeAddress: {
        lineAddress: "",
        cityAddress: "",
        districtAddress: "",
        stateAddress: "",
        countryAddress: "",
        postalCode: "",
      },
      dataContactInfo: {
        patientPhoneNumber: "",
        patientPhoneNumberType: "",
        patientEmail: "",
        approval: "",
      },
    };
  },
  methods: {
    updatePersonalInfoData(personalInfoPayload) {
      this.dataPersonalInfo = personalInfoPayload;
    },
    updateEmergencyContactData(emergencyContactPayload) {
      this.dataEmergencyContact = emergencyContactPayload;
    },
    updateHomeAddressData(homeAddressPayload) {
      this.dataHomeAddress = homeAddressPayload;
    },
    updateContactInfoData(contactInfoPayload) {
      this.dataContactInfo = contactInfoPayload;
    },
  },
  mounted() {
    EventBus.$on("DATA_PERSONAL_INFO_PUBLISHED", (personalInfoPayload) => {
      this.updatePersonalInfoData(personalInfoPayload);
    }),
      EventBus.$on(
        "DATA_EMERGENCY_CONTACT_INFO_PUBLISHED",
        (emergencyContactPayload) => {
          this.updateEmergencyContactData(emergencyContactPayload);
        }
      ),
      EventBus.$on("DATA_ADDRESS_INFO_PUBLISHED", (homeAddressPayload) => {
        this.updateHomeAddressData(homeAddressPayload);
      }),
      EventBus.$on("DATA_CONTACT_INFO_PUBLISHED", (contactInfoPayload) => {
        this.updateContactInfoData(contactInfoPayload);
      });
  },
};
</script>
