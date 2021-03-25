<template>
  <v-container fluid>
    <v-row align="center" justify="center"> </v-row>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">Household Address</div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium">
          Address Type:
          <span class="font-weight-regular">{{
            dataHouseholdHomeAddress.addressType
          }}</span>
        </div>
        <div class="font-weight-regular">
          {{ dataHouseholdHomeAddress.lineAddress1 }}
        </div>
        <template v-if="dataHouseholdHomeAddress.lineAddress2">
          <div class="font-weight-regular">
            {{ dataHouseholdHomeAddress.lineAddress2 }}
          </div>
        </template>
        <div class="font-weight-regular">
          {{ dataHouseholdHomeAddress.cityAddress }},
          {{ dataHouseholdHomeAddress.stateAddress }},
          {{ dataHouseholdHomeAddress.countryAddress }},
          {{ dataHouseholdHomeAddress.postalCode }}
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" sm="6" md="3">
        <div class="font-weight-medium primary--text">
          Household Contact Information
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium">
          Primary Phone:
          <span
            v-if="dataHouseholdContactInfo.primaryPhoneNumber"
            class="font-weight-regular"
            >{{ dataHouseholdContactInfo.primaryPhoneNumber }}
            <span v-if="dataHouseholdContactInfo.primaryPhoneNumberType"
              >({{ dataHouseholdContactInfo.primaryPhoneNumberType }})</span
            ></span
          ><span v-else class="font-weight-regular">Not provided</span>
        </div>
        <div class="font-weight-medium">
          Secondary Phone:
          <span
            v-if="dataHouseholdContactInfo.secondaryPhoneNumber"
            class="font-weight-regular"
            >{{ dataHouseholdContactInfo.secondaryPhoneNumber }}
            <span v-if="dataHouseholdContactInfo.secondaryPhoneNumberType"
              >({{ dataHouseholdContactInfo.secondaryPhoneNumberType }})</span
            ></span
          ><span v-else class="font-weight-regular">Not provided</span>
        </div>
        <div class="font-weight-medium">
          Primary E-mail:
          <span
            v-if="dataHouseholdContactInfo.primaryEmail"
            class="font-weight-regular"
            >{{ dataHouseholdContactInfo.primaryEmail }}</span
          >
          <span v-else class="font-weight-regular">Not provided</span>
        </div>
        <div class="font-weight-medium">
          Secondary E-mail:
          <span
            v-if="dataHouseholdContactInfo.secondaryEmail"
            class="font-weight-regular"
            >{{ dataHouseholdContactInfo.secondaryEmail }}</span
          >
          <span v-else class="font-weight-regular">Not provided</span>
        </div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-card class="d-flex align-end flex-wrap" flat>
          <template v-for="(member, i) in dataHouseholdPersonalInfo">
            <v-card class="pa-2" flat min-width="33%" :key="i">
              <v-img
                contain
                height="300"
                max-width="300"
                :src="member ? member.patientPhotoSrc : undefined"
                v-bind:class="{
                  hidden: member ? !member.patientPhotoSrc : true,
                }"
              >
              </v-img>
              <div class="font-weight-medium primary--text">
                Personal Info: Household Member #{{ i + 1 }}
              </div>
              <div class="font-weight-medium">
                Name:
                <span class="font-weight-regular"
                  >{{ member.familyName }},
                  {{ member.givenName }}
                  {{ member.middleName }}
                  {{ member.suffix }}</span
                >
              </div>
              <div class="font-weight-medium">
                Date of Birth:
                <span class="font-weight-regular">{{ member.birthDate }}</span>
              </div>
              <div class="font-weight-medium">
                Gender ID:
                <span class="font-weight-regular">{{ member.gender }}</span>
              </div>
              <div class="font-weight-medium">
                Race:
                <span class="font-weight-regular">{{ member.race }}</span>
              </div>
              <div class="font-weight-medium">
                Ethnicity:
                <span class="font-weight-regular">{{ member.ethnicity }}</span>
              </div>
              <div class="font-weight-medium">
                Preferred Language:
                <span class="font-weight-regular">{{
                  member.preferredLanguage
                }}</span>
              </div>
              <div class="font-weight-light">Emergency Contact</div>
              <div class="font-weight-medium">
                Name:
                <span v-if="i === 0" class="font-weight-regular"
                  >{{
                    dataHouseholdEmergencyContact.emergencyContactFamilyName
                  }},
                  {{ dataHouseholdEmergencyContact.emergencyContactGivenName
                  }}<span
                    v-if="
                      dataHouseholdEmergencyContact.emergencyContactRelationship
                    "
                  >
                    (Relationship:
                    {{
                      dataHouseholdEmergencyContact.emergencyContactRelationship
                    }})</span
                  ></span
                ><span v-else class="font-weight-regular"
                  >{{ dataHouseholdPersonalInfo[0].familyName }},
                  {{ dataHouseholdPersonalInfo[0].givenName }} (Relationship:
                  {{ member.relationship }})</span
                >
              </div>
              <div class="font-weight-medium">
                Phone:
                <span v-if="i === 0" class="font-weight-regular"
                  >{{
                    dataHouseholdEmergencyContact.emergencyContactPhoneNumber
                  }}
                  <span
                    v-if="
                      dataHouseholdEmergencyContact.emergencyContactPhoneNumberType
                    "
                    >({{
                      dataHouseholdEmergencyContact.emergencyContactPhoneNumberType
                    }})</span
                  ></span
                >
                <span v-else class="font-weight-regular"
                  >{{ dataHouseholdContactInfo.primaryPhoneNumber }}
                  <span v-if="dataHouseholdContactInfo.primaryPhoneNumberType"
                    >({{
                      dataHouseholdContactInfo.primaryPhoneNumberType
                    }})</span
                  ></span
                >
              </div>
              <v-card-actions>
                <v-btn
                  outlined
                  small
                  color="primary"
                  @click="editPersonalInfo(i + 1)"
                >
                  Edit
                </v-btn>
              </v-card-actions>
            </v-card>
          </template>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <p></p>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from "../../eventBus";
import brokerRequests from "../../brokerRequests";

export default {
  data() {
    return {
      dataHouseholdPersonalInfo: [],
      dataHouseholdEmergencyContact: {
        emergencyContactFamilyName: "",
        emergencyContactGivenName: "",
        emergencyContactPhoneNumber: "",
        emergencyContactPhoneNumberType: "",
      },
      dataHouseholdHomeAddress: {
        addressType: "",
        lineAddress: "",
        cityAddress: "",
        stateAddress: "",
        countryAddress: "",
        postalCode: "",
      },
      dataHouseholdContactInfo: {
        primaryPhoneNumber: "",
        primaryPhoneNumberType: "",
        primaryEmail: "",
        secondaryPhoneNumber: "",
        secondaryPhoneNumberType: "",
        secondaryEmail: "",
      },
    };
  },
  props: {
    numberOfHouseholdMembers: Number,
  },
  methods: {
    updateHomeAddressData(householdHomeAddressPayload) {
      this.dataHouseholdHomeAddress = householdHomeAddressPayload;
    },
    updateHouseholdContactInfoData(householdContactInfoPayload) {
      this.dataHouseholdContactInfo = householdContactInfoPayload;
    },
    updateHouseholdPersonalInfoData(
      householdPersonalInfoPayload,
      householdMemberNumber
    ) {
      // if member is next in line, add to array of members
      if (householdMemberNumber - 1 == this.dataHouseholdPersonalInfo.length) {
        this.dataHouseholdPersonalInfo.push(householdPersonalInfoPayload);
        // else if member already exists in array, update the member
      } else if (
        householdMemberNumber - 1 <
        this.dataHouseholdPersonalInfo.length
      ) {
        this.$set(
          this.dataHouseholdPersonalInfo,
          householdMemberNumber - 1,
          householdPersonalInfoPayload
        );
      }
    },
    updateHouseholdEmergencyContactData(householdEmergencyContactPayload) {
      this.dataHouseholdEmergencyContact = householdEmergencyContactPayload;
    },
    getNumberOfHouseholdMembers() {
      return this.numberOfHouseholdMembers;
    },
    editPersonalInfo(householdMemberNumber) {
      EventBus.$emit(
        "DATA_HOUSEHOLD_PERSONAL_INFO_EDIT",
        householdMemberNumber
      );
    },
    dataPayload() {
      let data = { Patient: [] };
      for (let [index, member] of this.dataHouseholdPersonalInfo.entries()) {
        let patient = {
          family: member.familyName,
          given: member.givenName,
          middle: member.middleName,
          suffix: member.suffix,
          gender: member.gender,
          birthDate: member.birthDate,
          race: member.race,
          ethnicity: member.ethnicity,
          language: member.preferredLanguage,
          relationship: member.relationship,
        };

        if (!index) {
          patient.phone = [];
          if (this.dataHouseholdContactInfo.primaryPhoneNumber)
            patient.phone.push({
              value: this.dataHouseholdContactInfo.primaryPhoneNumber,
              use: this.dataHouseholdContactInfo.primaryPhoneNumberType,
            });
          if (this.dataHouseholdContactInfo.secondaryPhoneNumber)
            patient.phone.push({
              value: this.dataHouseholdContactInfo.secondaryPhoneNumber,
              use: this.dataHouseholdContactInfo.secondaryPhoneNumberType,
            });
          patient.email = [];
          if (this.dataHouseholdContactInfo.primaryEmail)
            patient.email.push(this.dataHouseholdContactInfo.primaryEmail);
          if (this.dataHouseholdContactInfo.secondaryEmail)
            patient.email.push(this.dataHouseholdContactInfo.secondaryEmail);
        }

        patient.contact = {
          family: this.dataHouseholdEmergencyContact.emergencyContactFamilyName,
          given: this.dataHouseholdEmergencyContact.emergencyContactGivenName,
          phone: {
            value: this.dataHouseholdEmergencyContact
              .emergencyContactPhoneNumber,
            use: this.dataHouseholdEmergencyContact
              .emergencyContactPhoneNumberType,
          },
        };

        patient.address = {
          use: this.dataHouseholdHomeAddress.addressType,
          line: this.dataHouseholdHomeAddress.lineAddress1,
          city: this.dataHouseholdHomeAddress.cityAddress,
          state: this.dataHouseholdHomeAddress.stateAddress,
          postalCode: this.dataHouseholdHomeAddress.postalCode,
          country: this.dataHouseholdHomeAddress.countryAddress,
        };
        if (this.dataHouseholdHomeAddress.lineAddress2)
          patient.address.line += ` ${this.dataHouseholdHomeAddress.lineAddress2}`;

        data.Patient.push(patient);
      }

      return data;
    },
    submitPatientInfo() {
      let data = this.dataPayload();
      brokerRequests.submitRegistration(data).then((response) => {
        if (response.data) {
          this.onSuccess(response.data);
        } else if (response.error) {
          console.log(response.error);
          alert("Registration not successful");
          this.$emit("householdRegistrationFail");
        }
      });
    },
    onSuccess(data) {
      console.log(JSON.stringify(data));
      this.$emit("householdRegistrationSuccess", data.Patient);
    },
  },
  mounted() {
    EventBus.$on(
      "DATA_HOUSEHOLD_ADDRESS_INFO_PUBLISHED",
      (householdHomeAddressPayload) => {
        this.updateHomeAddressData(householdHomeAddressPayload);
      }
    ),
      EventBus.$on(
        "DATA_HOUSEHOLD_CONTACT_INFO_PUBLISHED",
        (householdContactInfoPayload) => {
          this.updateHouseholdContactInfoData(householdContactInfoPayload);
        }
      ),
      EventBus.$on(
        "DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED",
        (householdPersonalInfoPayload, householdMemberNumber) => {
          this.updateHouseholdPersonalInfoData(
            householdPersonalInfoPayload,
            householdMemberNumber
          );
        }
      ),
      EventBus.$on(
        "DATA_HOUSEHOLD_EMERGENCY_CONTACT_INFO_PUBLISHED",
        (householdEmergencyContactPayload) => {
          this.updateHouseholdEmergencyContactData(
            householdEmergencyContactPayload
          );
        }
      );
  },
};
</script>
<style lang="css" scoped>
.hidden {
  display: none;
}
</style>
