<template>
  <v-app id="Patient-registration">
    <v-main>
      <SystemBar />
      <v-container fill-height fluid>
        <v-layout>
          <v-row>
            <v-col cols="12">
              <Header />
              <!-- TODO: move stepper to new page, one for each flow? (or combine flows?) -->
              <v-stepper v-model="page" class="elevation-0">
                <v-stepper-header class="elevation-0">
                  <template
                    v-if="
                      isSinglePatientRegistration() || isHouseholdRegistration()
                    "
                  >
                    <template v-for="n in getNumberOfSteps()">
                      <v-stepper-step
                        :key="`${n}-step`"
                        :complete="page > n"
                        color="accent"
                        :step="n"
                      >
                      </v-stepper-step>

                      <v-divider
                        v-if="n !== getNumberOfSteps()"
                        :key="n"
                      ></v-divider>
                    </template>
                  </template>
                </v-stepper-header>

                <!--The v-stepper-items holds all of the "page" content we will swap in an out based on the navigation-->
                <v-stepper-items>
                  <!-- Greeting Page -->
                  <v-stepper-content step="1">
                    <v-card flat>
                      <GreetingPage
                        @singleRegistration="setSinglePatientRegistration()"
                        @householdRegistration="setHouseholdRegistration()"
                        ref="greetingpage"
                      />
                    </v-card>
                  </v-stepper-content>

                  <!--Logic to check for the "single patient" registration path-->
                  <template v-if="isSinglePatientRegistration()">
                    <!-- Single Patient: Home Address -->
                    <v-stepper-content step="2">
                      <v-toolbar flat>
                        <v-toolbar-title class="text-wrap"
                          >Enter your address</v-toolbar-title
                        >
                      </v-toolbar>
                      <v-card flat
                        ><SinglePatientHomeAddress
                          ref="singlepatienthomeaddress"
                      /></v-card>
                    </v-stepper-content>

                    <!-- Single Patient: Contact Info -->
                    <v-stepper-content step="3">
                      <v-toolbar flat>
                        <v-toolbar-title class="text-wrap"
                          >Enter your contact information</v-toolbar-title
                        >
                      </v-toolbar>
                      <v-card flat
                        ><SinglePatientContactInfo
                          ref="singlepatientcontactinfo"
                      /></v-card>
                    </v-stepper-content>

                    <!-- Single Patient: Personal Info -->
                    <v-stepper-content step="4">
                      <v-toolbar flat>
                        <v-toolbar-title class="text-wrap"
                          >Enter your personal information</v-toolbar-title
                        >
                      </v-toolbar>
                      <v-card flat
                        ><SinglePatientPersonalInfo
                          ref="singlepatientpersonalinfo"
                      /></v-card>
                    </v-stepper-content>

                    <!-- Single Patient: Eligibility Questions -->
                  <v-stepper-content step="5">
                    <v-toolbar flat>
                      <v-toolbar-title class="text-wrap"
                        >Please complete these eligibility
                        questions</v-toolbar-title
                      >
                    </v-toolbar>
                    <v-card flat
                      ><EligibilityQuestions ref="singlepatienteligibilityquestions"
                    /></v-card>
                  </v-stepper-content>

                  <!-- Single Patient: Screening Questions -->
                  <v-stepper-content step="6">
                    <v-toolbar flat>
                      <v-toolbar-title class="text-wrap"
                        >Please complete these screening
                        questions</v-toolbar-title
                      >
                    </v-toolbar>
                    <v-card flat
                      ><ScreeningQuestions ref="singlepatientscreeningquestions"
                    /></v-card>
                  </v-stepper-content>

                  <!-- Single Patient: Emergency Contact -->
                  <v-stepper-content step="7">
                    <v-toolbar flat>
                      <v-toolbar-title class="text-wrap">
                        Specify an emergency contact</v-toolbar-title
                      >
                    </v-toolbar>
                    <v-card flat
                      ><SinglePatientEmergencyContact
                        ref="singlepatientemergencycontact"
                    /></v-card>
                  </v-stepper-content>

                    <!-- Single Patient: Review and Submit -->
                    <v-stepper-content step="8">
                      <v-toolbar flat>
                        <v-toolbar-title class="text-wrap"
                          >Please ensure your information is
                          correct</v-toolbar-title
                        >
                      </v-toolbar>
                      <v-card flat
                        ><SinglePatientReviewSubmit
                          ref="singlePatientReviewSubmit"
                          @singlePatientRegistrationSuccess="
                            SinglePatientRegistrationSuccessful
                          "
                      /></v-card>
                    </v-stepper-content>

                    <!-- Single Patient: Follow up -->
                    <v-stepper-content step="9">
                      <v-toolbar flat>
                        <v-toolbar-title class="text-wrap"
                          >Download your QR code</v-toolbar-title
                        >
                      </v-toolbar>
                      <v-card flat
                        ><SinglePatientFollowUp ref="singlePatientFollowUp"
                      /></v-card>
                    </v-stepper-content>
                  </template>

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
                          >Enter your household contact
                          information</v-toolbar-title
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

                    <!-- Household: Eligibility Questions -->
                  <v-stepper-content step="6">
                    <v-toolbar flat>
                      <v-toolbar-title class="text-wrap"
                        >Please complete these eligibility
                        questions</v-toolbar-title
                      >
                    </v-toolbar>
                    <v-card flat
                      ><EligibilityQuestions ref="householdeligibilityquestions"
                    /></v-card>
                  </v-stepper-content>

                    <!-- Household: Emergency Contact -->
                  <v-stepper-content step="7">
                    <v-toolbar flat>
                      <v-toolbar-title class="text-wrap"
                        >Specify your emergency contact</v-toolbar-title
                      ></v-toolbar
                      >
                      <v-toolbar flat>
                        <v-subheader
                          >Note: You will be specified as the emergency contact
                          for the rest of your household.</v-subheader
                        >
                      </v-toolbar>
                      <v-card flat
                        ><HouseholdEmergencyContact
                          ref="householdemergencycontact"
                      /></v-card>
                    </v-stepper-content>

                    <!-- Household: Personal Info #n -->
                    <template v-for="n in getNumberOfHouseholdMembers() - 1">
                      <v-stepper-content :key="`${n + 1}-member`" :step="n + 7">
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
                          >Please ensure your information is
                          correct</v-toolbar-title
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
            </v-col>
            <v-col cols="12">
              <!--navigation footer along the bottom of the page -->
              <v-footer absolute color="white">
                <template v-if="isGreetingPage()">
                  <v-spacer></v-spacer>
                  <v-btn
                    color="secondary"
                    class="ma-2 white--text"
                    @click="goToNextPage()"
                  >
                    Continue
                    <v-icon right large color="white">
                      mdi-chevron-right
                    </v-icon>
                  </v-btn>
                </template>
                <template
                  v-else-if="
                    isSinglePatientReviewSubmit() &&
                      isSinglePatientRegistration()
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
                      <v-card-title
                        class="headline grey lighten-2 justify-center"
                      >
                        Are you sure you want to submit?
                      </v-card-title>
                      <v-card-text class="text-center">
                        Make sure that the information you provided is correct
                        and you want to proceed.
                        <br />
                        <br />
                        After submission you will receive a notification to
                        schedule your COVID vaccination appointment if you are
                        not already eligible to do so based upon CDC and local
                        Phases. Upon receiving your eligibility notification,
                        you will answer COVID screening questions, provide
                        consent, and choose your date to receive the COVID-19
                        vaccine.
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
                          @click="submitSinglePatientRegistration()"
                        >
                          Yes
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </template>
                <template
                  v-else-if="
                    isSinglePatientFollowUp() && isSinglePatientRegistration()
                  "
                >
                  <!--display no buttons-->
                </template>
                <template
                  v-else-if="
                    isHouseholdPatientReviewSubmit() &&
                      isHouseholdRegistration()
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
                      <v-card-title
                        class="headline grey lighten-2 justify-center"
                      >
                        Are you sure you want to submit?
                      </v-card-title>
                      <v-card-text class="text-center">
                        Make sure that the information you provided is correct
                        and you want to proceed.
                        <br />
                        <br />
                        After submission you will receive a notification to
                        schedule your COVID vaccination appointment if you are
                        not already eligible to do so based upon CDC and local
                        Phases. Upon receiving your eligibility notification,
                        you will answer COVID screening questions, provide
                        consent, and choose your date to receive the COVID-19
                        vaccine.
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
                <template
                  v-else-if="isHouseholdFollowUp() && isHouseholdRegistration()"
                >
                  <!--display no buttons-->
                </template>
                <template v-else>
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
                  <v-btn
                    color="secondary"
                    class="ma-2 white--text"
                    @click="goToNextPage()"
                  >
                    Continue
                    <v-icon right large color="white">
                      mdi-chevron-right
                    </v-icon>
                  </v-btn>
                </template>
              </v-footer>
            </v-col>
          </v-row>
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import GreetingPage from "./components/GreetingPage";
import SinglePatientHomeAddress from "./components/SinglePatientHomeAddress";
import SinglePatientContactInfo from "./components/SinglePatientContactInfo";
import SinglePatientPersonalInfo from "./components/SinglePatientPersonalInfo";
import SinglePatientEmergencyContact from "./components/SinglePatientEmergencyContact";
import SinglePatientReviewSubmit from "./components/SinglePatientReviewSubmit";
import SinglePatientFollowUp from "./components/SinglePatientFollowUp";
import HouseholdRegisterNumber from "./components/HouseholdRegisterNumber";
import HouseholdHomeAddress from "./components/HouseholdHomeAddress";
import HouseholdContactInfo from "./components/HouseholdContactInfo";
import HouseholdPersonalInfo_1 from "./components/HouseholdPersonalInfo_1";
import HouseholdEmergencyContact from "./components/HouseholdEmergencyContact";
import HouseholdPersonalInfo_n from "./components/HouseholdPersonalInfo_n";
import HouseholdReviewSubmit from "./components/HouseholdReviewSubmit";
import HouseholdFollowUp from "./components/HouseholdFollowUp";
import ScreeningQuestions from "./components/ScreeningQuestions";
import EligibilityQuestions from "./components/EligibilityQuestions";
import config from "./config.js";
import EventBus from "./eventBus";
import Header from "@/pages/application/partials/Header";
import SystemBar from "@/pages/application/partials/SystemBar";
export default {
  name: "App",
  methods: {
    submitSinglePatientRegistration() {
      this.$refs.singlePatientReviewSubmit.submitPatientInfo();
    },
    SinglePatientRegistrationSuccessful(data) {
      this.$refs.singlePatientFollowUp.updateQrCodeData(data);
      this.goToPage(config.registrationPages.SINGLE_PATIENT_FOLLOWUP_PAGE);
    },
    submitHouseholdRegistration() {
      this.$refs.householdReviewSubmit.submitPatientInfo();
    },
    householdRegistrationSuccessful(data) {
      this.$refs.householdFollowUp.updateQrCodeData(data);
      this.goToPage(
        config.registrationPages.HOUSEHOLD_FOLLOWUP_PAGE +
          this.getNumberOfHouseholdMembers() -
          2
      );
    },
    goToPage(pageNum) {
      this.page = pageNum;
    },
    setSinglePatientRegistration() {
      this.registrationPath =
        config.selectedRegistrationPath.SINGLE_PATIENT_REGISTRATION_PATH;
    },
    setHouseholdRegistration() {
      this.registrationPath =
        config.selectedRegistrationPath.HOUSEHOLD_REGISTRATION_PATH;
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
    getHouseholdFamilyName() {
      return this.familyName;
    },
    isSinglePatientRegistration() {
      let returnValue = true;
      this.registrationPath ==
      config.selectedRegistrationPath.SINGLE_PATIENT_REGISTRATION_PATH
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isHouseholdRegistration() {
      let returnValue = true;
      this.registrationPath ==
      config.selectedRegistrationPath.HOUSEHOLD_REGISTRATION_PATH
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isGreetingPage() {
      let returnValue = true;
      this.page == config.registrationPages.GREETING_PAGE
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isSinglePatientReviewSubmit() {
      let returnValue = true;
      this.page == config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isHouseholdPatientReviewSubmit() {
      let returnValue = true;
      this.page ==
      this.numberOfHouseholdMembers +
        config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isSinglePatientFollowUp() {
      let returnValue = true;
      this.page == config.registrationPages.SINGLE_PATIENT_FOLLOWUP_PAGE
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    isHouseholdFollowUp() {
      let returnValue = true;
      this.page ==
      config.registrationPages.HOUSEHOLD_FOLLOWUP_PAGE +
        this.getNumberOfHouseholdMembers() -
        2
        ? (returnValue = true)
        : (returnValue = false);
      return returnValue;
    },
    jumpToHouseholdPersonalInfoPage(householdMemberNumber) {
      if (householdMemberNumber == 1) {
        this.goToPage(
          config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE
        );
      } else {
        this.goToPage(
          config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE +
            householdMemberNumber -
            1
        );
      }
    },
    goToPreviousPage() {
      let currentPage = this.page;
      currentPage > config.registrationPages.GREETING_PAGE
        ? currentPage--
        : currentPage;

      this.goToPage(currentPage);
    },
    goToNextPage() {
      if (this.isSinglePatientRegistration()) {
        switch (this.page) {
          case config.registrationPages.GREETING_PAGE:
            if (this.$refs.greetingpage.verifyFormContents()) {
              this.goToPage(
                config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE
              );
            }
            break;
          case config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE:
            this.$refs.singlepatienthomeaddress.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE:
            this.$refs.singlepatientcontactinfo.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE:
            this.$refs.singlepatientpersonalinfo.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_ELIGIBILITY_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_ELIGIBILITY_PAGE:
            this.$refs.singlepatienteligibilityquestions.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_SCREENING_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_ELIGIBILITY_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_SCREENING_PAGE:
            this.$refs.singlepatientscreeningquestions.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_SCREENING_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE:
            this.$refs.singlepatientemergencycontact.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE
                )
              : this.goToPage(
                  config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE
                );
            break;
          case config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE:
            this.goToPage(
              config.registrationPages.SINGLE_PATIENT_FOLLOWUP_PAGE
            );
            break;
          default:
            alert(this.page);
            break;
        }
      } else if (this.isHouseholdRegistration()) {
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
          case this.page ==
            config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE:
            this.$refs.householdhomeaddress.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE
                )
              : this.goToPage(
                  config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE
                );
            break;
          case this.page ==
            config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE:
            this.$refs.householdcontactinfo.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages
                    .HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE
                )
              : this.goToPage(
                  config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE
                );
            break;
          case this.page ==
            config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE:
            this.$refs.householdPersonalInfo_1.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.HOUSEHOLD_ELIGIBILITY_PAGE
                )
              : this.goToPage(
                  config.registrationPages
                    .HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE
                );
            break;
            case this.page ==
            config.registrationPages.HOUSEHOLD_ELIGIBILITY_PAGE:
            this.$refs.householdeligibilityquestions.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE
                )
              : this.goToPage(
                  config.registrationPages
                    .HOUSEHOLD_ELIGIBILITY_PAGE
                );
            break;
          case this.page ==
            config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE:
            this.$refs.householdemergencycontact.verifyFormContents()
              ? this.goToPage(
                  config.registrationPages
                    .HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE
                )
              : this.goToPage(
                  config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE
                );
            break;
          case this.page >=
            config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE &&
            this.page <=
              this.getNumberOfHouseholdMembers() +
                config.registrationPages
                  .HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE -
                2:
            if (
              this.$refs.householdPersonalInfo[
                this.page -
                  config.registrationPages
                    .HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE
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
      } else {
        switch (this.page) {
          case config.registrationPages.GREETING_PAGE:
            this.$refs.greetingpage.verifyFormContents();
            break;
          default:
            alert(this.page);
            break;
        }
      }
    },
    getNumberOfSteps() {
      let numberOfSteps = 0;
      if (this.isSinglePatientRegistration()) {
        numberOfSteps = config.registrationPages.SINGLE_PATIENT_FOLLOWUP_PAGE;
      } else if (this.isHouseholdRegistration()) {
        numberOfSteps =
          this.numberOfHouseholdMembers +
          config.registrationPages.HOUSEHOLD_FOLLOWUP_PAGE -
          2;
      }

      return numberOfSteps;
    },
  },
  mounted() {
    EventBus.$on("DATA_HOUSEHOLD_COUNT_UPDATED", (householdCountPayload) => {
      this.setNumberOfHouseholdMembers(householdCountPayload);
    }),
      EventBus.$on("DATA_HOUSEHOLD_FAMILY_NAME", (familyName) => {
        this.setHouseholdFamilyName(familyName);
      }),
      EventBus.$on(
        "DATA_HOUSEHOLD_PERSONAL_INFO_EDIT",
        (householdMemberNumber) => {
          this.jumpToHouseholdPersonalInfoPage(householdMemberNumber);
        }
      );
  },
  components: {
    GreetingPage,
    SinglePatientPersonalInfo,
    SinglePatientHomeAddress,
    SinglePatientContactInfo,
    SinglePatientEmergencyContact,
    SinglePatientReviewSubmit,
    SinglePatientFollowUp,
    HouseholdRegisterNumber,
    HouseholdHomeAddress,
    HouseholdContactInfo,
    HouseholdPersonalInfo_1,
    HouseholdEmergencyContact,
    HouseholdPersonalInfo_n,
    HouseholdReviewSubmit,
    HouseholdFollowUp,
    ScreeningQuestions,
    EligibilityQuestions,
    Header,
    SystemBar,
  },
  computed: {
    titleFontSize() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return "1.5em";
        default:
          return "2.2em";
      }
    },
  },
  data() {
    return {
      dialog: false,
      page: config.registrationPages.GREETING_PAGE,
      registrationPath: config.selectedRegistrationPath.NO_PATH_SELECTED,
      numberOfHouseholdMembers: 2,
      familyName: "",
    };
  },
};
</script>
<style lang="css" scoped>
.v-btn:not(.v-btn--round).v-size--default {
  font-size: 1rem;
  min-width: 8rem;
}
.v-stepper__step {
  padding: 16px;
}
.v-system-bar {
  font-size: 2rem;
}
</style>
