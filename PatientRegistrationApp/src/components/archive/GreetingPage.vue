<template>
  <v-container fluid>
    <!--Intro Text -->
    <v-row align="center" justify="start"
      ><v-col cols="12">
        <div>
          <p style="font-size:1.3em" class="font-weight-medium">
            {{ welcome }}
          </p>
        </div>
        <div><br />{{ introStatement }}</div>
        <div><br />{{ expectationsStatement }}</div>
        <div><br />{{ finalStatement }}</div></v-col
      ></v-row
    >
    <v-col cols="12">
      <v-divider></v-divider>
    </v-col>
    <!--input label-->
    <v-col cols="0" sm="3" md="3" lg="3"></v-col>
    <v-row align="center" justify="start">
      <!--input label-->
      <v-col cols="0" sm="3" md="3" lg="3"> </v-col>
      <v-col cols="12" sm="9" md="9" lg="9">
        <span
          ><p style="font-size:1.3em" class="font-weight-regular">
            <span style="color:red">* </span>Which language would you like to
            communicate in during your vaccination?
          </p></span
        >
      </v-col>
      <!--input field-->
      <v-col cols="0" sm="3" md="3" lg="4"> </v-col>
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-select
          required
          dense
          :items="preferredLanguageOptions"
          :rules="[(v) => !!v || 'Preferred language field is required']"
          v-model="preferredLanguage"
        ></v-select>
      </v-col>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
      <!--input label-->
      <v-col cols="0" sm="3" md="3" lg="3"> </v-col>
      <v-col cols="12" sm="9" md="9" lg="9">
        <span
          ><p style="font-size:1.3em" class="font-weight-regular">
            <br /><span style="color:red">* </span>Select your registration type
          </p></span
        >
      </v-col>
      <!--input buttons-->
    </v-row>
    <v-row justify="center">
      <v-col cols="12">
        <v-card class="d-flex justify-center flex-wrap" flat>
          <v-card class="pa-2" flat>
            <v-btn
              :outlined="!isSingleRegistration"
              x-large
              color="primary"
              height="13em"
              width="17.5em"
              @click="singleRegistration"
            >
              <div>
                <v-icon size="7em">mdi-account</v-icon>
                <span
                  ><br />
                  <p style="font-size:1.1em" class="font-weight-regular">
                    <br />Register myself
                  </p></span
                >
              </div>
            </v-btn>
          </v-card>
          <v-card class="pa-2" flat>
            <v-tooltip top max-width="30%">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  :outlined="!isHouseholdRegistration"
                  x-large
                  color="primary"
                  v-bind="attrs"
                  v-on="on"
                  height="13em"
                  width="17.5em"
                  @click="householdRegistration"
                >
                  <div>
                    <v-icon size="7em">mdi-account-group</v-icon>
                    <span
                      ><br />
                      <p style="font-size:1.1em" class="font-weight-regular">
                        <br />Register my household
                      </p></span
                    >
                  </div>
                </v-btn>
              </template>
              <span>{{ householdDefinition }}</span>
            </v-tooltip>
          </v-card>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
import customerSettings from "../../customerSettings";

export default {
  name: "GreetingPage",
  data: () => ({
    preferredLanguageOptions: ["English", "Spanish"],
    isSingleRegistration: false,
    isHouseholdRegistration: false,
    householdDefinition: customerSettings.householdDefinition,
    welcome: customerSettings.welcome,
    introStatement: customerSettings.introStatement,
    expectationsStatement: customerSettings.expectationsStatement,
    finalStatement: customerSettings.finalStatement,
  }),
  computed: {
    preferredLanguage: {
      get() {
        return this.$store.state.preferredLanguage;
      },
      set(value) {
        this.$store.commit("setPreferredLanguage", {
          index: 0,
          lang: this.preferredLanguage,
        });
      },
    },
  },
  methods: {
    singleRegistration() {
      this.isHouseholdRegistration = false;
      this.isSingleRegistration = true;
      this.$emit("singleRegistration");
    },
    householdRegistration() {
      this.isSingleRegistration = false;
      this.isHouseholdRegistration = true;
      this.$emit("householdRegistration");
    },
    verifyFormContents() {
      var valid = true;
      var message = "Woops! You need to enter the following field(s):";

      if (this.preferredLanguage == "") {
        message += " Preferred Language";
        valid = false;
      }
      if (
        this.isSingleRegistration == false &&
        this.isHouseholdRegistration == false
      ) {
        if (!valid) {
          message += ",";
        }
        message += " Registration Type";
        valid = false;
      }
      if (valid == false) {
        alert(message);
        return false;
      }
    },
  },
};
</script>
<style lang="css" scoped>
.v-select >>> .v-select__selections {
  font-size: 1em;
  line-height: 1.1em;
}
.v-select >>> .v-select__slot {
  font-size: 1.5em;
}
.v-select >>> .v-list-item__title {
  font-size: 1.5em;
}
</style>
