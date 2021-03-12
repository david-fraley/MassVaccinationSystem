<template>
  <v-app id="Patient-registration">
    <v-main>
      <v-container fill-height fluid>
        <v-layout>
          <v-row>
            <v-col cols="12">
              <Header />
              <v-card flat>
                <GreetingPage
                  @singleRegistration="setSinglePatientRegistration()"
                  @householdRegistration="setHouseholdRegistration()"
                  ref="greetingpage"
                />
              </v-card>
            </v-col>
          </v-row>
          <!--navigation footer along the bottom of the page -->
          <v-footer fixed color="white">
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
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import GreetingPage from "@/components/GreetingPage";
import config from "@/config.js";
import Header from "@/pages/application/partials/Header";

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
      switch (this.page) {
        case config.registrationPages.GREETING_PAGE:
          this.$refs.greetingpage.verifyFormContents();
          break;
        default:
          alert(this.page);
          break;
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
    Header,
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
</style>
