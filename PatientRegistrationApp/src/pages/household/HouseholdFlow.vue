<template>
  <v-row>
    <v-col cols="12">
      <v-stepper v-model="page" class="elevation-0">
        <v-stepper-header class="elevation-0">
          <template v-for="n in getNumberOfSteps()">
            <v-stepper-step
              :key="`${n}-step`"
              :complete="page > n"
              color="accent"
              :step="n"
            >
            </v-stepper-step>

            <v-divider v-if="n !== getNumberOfSteps()" :key="n"></v-divider>
          </template>
        </v-stepper-header>
        <v-stepper-items>
          <!--Logic to check for the "household" registration path-->
          <template v-if="isHouseholdRegistration()">
            <!-- Household: Register Number of People -->
            <v-stepper-content step="2">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Register your household</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdRegisterNumber ref="householdregisternumber"
              /></v-card>
            </v-stepper-content>

            <!-- Household: Address -->
            <v-stepper-content step="3">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Enter your household address</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdHomeAddress ref="householdhomeaddress"
              /></v-card>
            </v-stepper-content>

            <!-- Household: Contact Info -->
            <v-stepper-content step="4">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Enter your household contact information</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdContactInfo ref="householdcontactinfo"
              /></v-card>
            </v-stepper-content>

            <!-- Household: Personal Info -->
            <v-stepper-content step="5">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Enter your personal information</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdPersonalInfo_1 ref="householdPersonalInfo_1"
              /></v-card>
            </v-stepper-content>

            <!-- Household: Emergency Contact -->
            <v-stepper-content step="6">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Specify your emergency contact</v-toolbar-title
                ></v-toolbar
              >
              <v-toolbar flat>
                <v-subheader
                  >Note: You will be specified as the emergency contact for the
                  rest of your household.</v-subheader
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdEmergencyContact ref="householdemergencycontact"
              /></v-card>
            </v-stepper-content>

            <!-- Household: Personal Info #n -->
            <template v-for="n in getNumberOfHouseholdMembers() - 1">
              <v-stepper-content :key="`${n + 1}-member`" :step="n + 6">
                <v-toolbar flat>
                  <v-toolbar-title class="text-wrap"
                    >Enter personal information for household member #{{
                      n + 1
                    }}</v-toolbar-title
                  >
                </v-toolbar>
                <v-card flat
                  ><HouseholdPersonalInfo_n
                    ref="householdPersonalInfo"
                    v-bind:householdMemberNumber="n + 1"
                  ></HouseholdPersonalInfo_n
                ></v-card>
              </v-stepper-content>
            </template>

            <!-- Household: Review and submit -->
            <v-stepper-content :step="getNumberOfSteps() - 1">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Please ensure your information is correct</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdReviewSubmit
                  ref="householdReviewSubmit"
                  v-bind:numberOfHouseholdMembers="
                    getNumberOfHouseholdMembers()
                  "
                  @householdRegistrationSuccess="
                    householdRegistrationSuccessful
                  "
              /></v-card>
            </v-stepper-content>

            <!-- Household: Follow up -->
            <v-stepper-content :step="getNumberOfSteps()">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Download your QR code</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><HouseholdFollowUp
                  v-bind:numberOfHouseholdMembers="
                    getNumberOfHouseholdMembers()
                  "
                  ref="householdFollowUp"
              /></v-card>
            </v-stepper-content>
          </template>
        </v-stepper-items>
      </v-stepper>
      <v-footer>
          <template
              v-else-if="
                isHouseholdPatientReviewSubmit() && isHouseholdRegistration()
              "
            >
              <v-btn
                color="secondary"
                class="ma-2 white--text"
                @click="goToPreviousPage()"
              >
                <v-icon left large color="white">
                  mdi-chevron-left
                </v-icon>
                Back
              </v-btn>
              <v-spacer></v-spacer>
              <v-dialog v-model="dialog" width="40rem">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="secondary"
                    class="ma-2 white--text"
                    v-bind="attrs"
                    v-on="on"
                  >
                    Submit
                  </v-btn>
                </template>
                <v-card>
                  <v-card-title class="headline grey lighten-2 justify-center">
                    Are you sure you want to submit?
                  </v-card-title>
                  <v-card-text class="text-center">
                    Make sure that the information you provided is correct and
                    you want to proceed.
                    <br />
                    <br />
                    After submission you will receive a notification to schedule
                    your COVID vaccination appointment if you are not already
                    eligible to do so based upon CDC and local Phases. Upon
                    receiving your eligibility notification, you will answer
                    COVID screening questions, provide consent, and choose your
                    date to receive the COVID-19 vaccine.
                  </v-card-text>
                  <v-divider></v-divider>
                  <v-card-actions>
                    <v-btn color="primary" text @click="dialog = false">
                      No
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="primary"
                      text
                      @click="submitHouseholdRegistration()"
                    >
                      Yes
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </template>
      </v-footer>
    </v-col>
  </v-row>
</template>

<script>
import HouseholdRegisterNumber from "@/components/HouseholdRegisterNumber";
import HouseholdHomeAddress from "@/components/HouseholdHomeAddress";
import HouseholdContactInfo from "@/components/HouseholdContactInfo";
import HouseholdPersonalInfo_1 from "@/components/HouseholdPersonalInfo_1";
import HouseholdEmergencyContact from "@/components/HouseholdEmergencyContact";
import HouseholdPersonalInfo_n from "@/components/HouseholdPersonalInfo_n";
import HouseholdReviewSubmit from "@/components/HouseholdReviewSubmit";
import HouseholdFollowUp from "@/components/HouseholdFollowUp";
import config from "@/config.js";

export default {
  name: "HouseholdFlow",
  components: {
    HouseholdRegisterNumber,
    HouseholdHomeAddress,
    HouseholdContactInfo,
    HouseholdPersonalInfo_1,
    HouseholdEmergencyContact,
    HouseholdPersonalInfo_n,
    HouseholdReviewSubmit,
    HouseholdFollowUp,
  },
  methods: {
    householdRegistrationSuccessful(data) {
      this.$refs.householdFollowUp.updateQrCodeData(data);
      this.goToPage(
        config.registrationPages.HOUSEHOLD_FOLLOWUP_PAGE +
          this.getNumberOfHouseholdMembers() -
          2
      );
    },
    setNumberOfHouseholdMembers(householdCountPayload) {
      if (
        this.numberOfHouseholdMembers != householdCountPayload.householdCount
      ) {
        this.numberOfHouseholdMembers = householdCountPayload.householdCount;
      }
    },
    getNumberOfHouseholdMembers() {
      return this.numberOfHouseholdMembers;
    },
    setHouseholdFamilyName(familyName) {
      this.familyName = familyName;

      for (var i = 0; i < this.getNumberOfHouseholdMembers(); i++) {
        this.$refs.householdPersonalInfo[i].setHouseholdFamilyName(
          this.familyName
        );
      }
    },
    goToPage(pageNum) {
      this.page = pageNum;
    },
    getHouseholdFamilyName() {
      return this.familyName;
    },
    goToNextPage() {
      switch (true) {
        case this.page == config.registrationPages.GREETING_PAGE:
          if (this.$refs.greetingpage.verifyFormContents()) {
            this.goToPage(
              config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE
            );
          }
          break;
        case this.page ==
          config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE:
          this.$refs.householdregisternumber.verifyFormContents()
            ? this.goToPage(
                config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE
              )
            : this.goToPage(
                config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE
              );
          break;
        case this.page == config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE:
          this.$refs.householdhomeaddress.verifyFormContents()
            ? this.goToPage(
                config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE
              )
            : this.goToPage(
                config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE
              );
          break;
        case this.page == config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE:
          this.$refs.householdcontactinfo.verifyFormContents()
            ? this.goToPage(
                config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE
              )
            : this.goToPage(
                config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE
              );
          break;
        case this.page ==
          config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE:
          this.$refs.householdPersonalInfo_1.verifyFormContents()
            ? this.goToPage(
                config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE
              )
            : this.goToPage(
                config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE
              );
          break;
        case this.page ==
          config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE:
          this.$refs.householdemergencycontact.verifyFormContents()
            ? this.goToPage(
                config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE
              )
            : this.goToPage(
                config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE
              );
          break;
        case this.page >=
          config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE &&
          this.page <=
            this.getNumberOfHouseholdMembers() +
              config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE -
              2:
          if (
            this.$refs.householdPersonalInfo[
              this.page -
                config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE
            ].verifyFormContents()
          ) {
            this.page++;
          }
          break;
        case this.page == this.getNumberOfSteps() - 1:
          this.page++;
          break;
        default:
          alert(this.page);
          break;
      }
    },
  },
};
</script>

<style></style>
