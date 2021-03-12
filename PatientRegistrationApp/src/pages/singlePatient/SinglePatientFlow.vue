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
          <!-- Greeting Page -->
          <v-stepper-content step="1"> </v-stepper-content>

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
                ><SinglePatientHomeAddress ref="singlepatienthomeaddress"
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
                ><SinglePatientContactInfo ref="singlepatientcontactinfo"
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
                ><SinglePatientPersonalInfo ref="singlepatientpersonalinfo"
              /></v-card>
            </v-stepper-content>

            <!-- Single Patient: Screening Questions -->
            <v-stepper-content step="5">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Please complete these screening questions</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><ScreeningQuestions ref="singlepatientscreeningquestions"
              /></v-card>
            </v-stepper-content>

            <!-- Single Patient: Emergency Contact -->
            <v-stepper-content step="6">
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
            <v-stepper-content step="7">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Please ensure your information is correct</v-toolbar-title
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
            <v-stepper-content step="8">
              <v-toolbar flat>
                <v-toolbar-title class="text-wrap"
                  >Download your QR code</v-toolbar-title
                >
              </v-toolbar>
              <v-card flat
                ><SinglePatientFollowUp ref="singlePatientFollowUp"
              /></v-card>
            </v-stepper-content>
          </template> </v-stepper-items></v-stepper
      ><v-footer>
        <template
          v-if="
            isSinglePatientReviewSubmit() && isSinglePatientRegistration()
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
                Make sure that the information you provided is correct and you
                want to proceed.
                <br />
                <br />
                After submission you will receive a notification to schedule
                your COVID vaccination appointment if you are not already
                eligible to do so based upon CDC and local Phases. Upon
                receiving your eligibility notification, you will answer COVID
                screening questions, provide consent, and choose your date to
                receive the COVID-19 vaccine.
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
          v-else-if="isSinglePatientFollowUp() && isSinglePatientRegistration()"
        >
          <!--display no buttons-->
        </template>
      </v-footer>
    </v-col>
  </v-row>
</template>

<script>
import SinglePatientHomeAddress from "@/components/SinglePatientHomeAddress";
import SinglePatientContactInfo from "@/components/SinglePatientContactInfo";
import SinglePatientPersonalInfo from "@/components/SinglePatientPersonalInfo";
import SinglePatientEmergencyContact from "@/components/SinglePatientEmergencyContact";
import SinglePatientReviewSubmit from "@/components/SinglePatientReviewSubmit";
import SinglePatientFollowUp from "@/components/SinglePatientFollowUp";
import ScreeningQuestions from "@/components/ScreeningQuestions";
import config from "@/config.js";

export default {
  name: "SinglePatientFlow",
  components: {
    SinglePatientPersonalInfo,
    SinglePatientHomeAddress,
    SinglePatientContactInfo,
    SinglePatientEmergencyContact,
    SinglePatientReviewSubmit,
    SinglePatientFollowUp,
    ScreeningQuestions,
  },
  data() {
    return {
      page: 1,
    };
  },
  methods: {
    goToPage(pageNum) {
      this.page = pageNum;
    },
    goToNextPage() {
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
                config.registrationPages.SINGLE_PATIENT_SCREENING_PAGE
              )
            : this.goToPage(
                config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE
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
          this.goToPage(config.registrationPages.SINGLE_PATIENT_FOLLOWUP_PAGE);
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
