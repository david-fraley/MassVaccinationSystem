<template>
  <v-container>
    <v-row>
      <v-col cols="3">
        <v-img
          max-height="220"
          max-width="200"
          src="../assets/blankPicture.png"
        >
        </v-img>
        <div class="font-weight-medium">
          Patient ID: <span class="font-weight-regular">{{ patientId }}</span>
        </div>
      </v-col>
      <v-col cols="9">
        <v-row no-gutters>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientLastName"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientFirstName"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientDateOfBirth"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Gender</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientGender"
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Age</div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientAge"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">
              Preferred Language
            </div>
          </v-col>
          <v-col cols="3">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientPreferredLanguage"
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address</div>
          </v-col>
          <v-col cols="9">
            <v-text-field
              filled
              dense
              readonly
              outlined
              :value="patientStreetAddress"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "PatientInfoComponent",
  computed: {
    patientId() {
      return this.$store.state.patientResource.id;
    },
    patientLastName() {
      return this.$store.state.patientResource.family;
    },
    patientFirstName() {
      return this.$store.state.patientResource.given;
    },
    patientDateOfBirth() {
      return this.$store.state.patientResource.birthDate;
    },
    patientAge() {
      let currentDate = new Date();

      let dateOfBirthString = this.$store.state.patientResource.birthDate;

      if (dateOfBirthString == "" || !dateOfBirthString) return " ";

      let dobYear = dateOfBirthString.substring(6, 10);
      let ageYears = currentDate.getFullYear() - dobYear;

      let dobMonth = dateOfBirthString.substring(0, 2);
      let currentMonth = currentDate.getMonth() + 1;
      let ageMonths;
      if (currentMonth >= dobMonth) {
        ageMonths = currentMonth - dobMonth;
      } else {
        ageYears--;
        ageMonths = 12 + currentMonth - dobMonth;
      }

      let ageString = ageYears + " yr(s). " + ageMonths + " mo(s).";
      return ageString;
    },
    patientGender() {
      return this.$store.state.patientResource.gender;
    },
    patientStreetAddress() {
      let address = this.$store.state.patientResource.address;
      let components = ["line", "city", "state", "postalCode"];

      return components
        .map((component) => address[component])
        .filter((v) => v)
        .join(", ");
    },
    patientPreferredLanguage() {
      return this.$store.state.patientResource.language;
    },
  },
  methods: {},
  data() {
    return {};
  },
};
</script>
