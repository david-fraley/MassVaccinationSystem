<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Retrieve Patient Record</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
        <v-col cols="12">
          <div class="font-weight-medium secondary--text">Retrieve the patient's record by scanning their QR code.</div>
        </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Scan QR code
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">If the QR code is not available, search for the patient record by name and/or DOB.</div>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">Last Name</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="LastName"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="DOB"
              placeholder="MM/DD/YYYY"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
      <!--blank column for spacing-->
      <v-col cols="1">
      </v-col>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">First Name</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="FirstName"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row> 	
    <v-row no-gutters>
      <v-col cols="12">
        <v-btn color="accent">
          Search
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        Lookup table goes here
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Retrieve patient record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'PatientLookupPage',
    methods: 
    {
    },
    components: 
    {
    },
    data () {
	return {
		patientFamilyName: '',
		patientGivenName: '',
		patientDate: '',
      }
    }
  }
</script>
