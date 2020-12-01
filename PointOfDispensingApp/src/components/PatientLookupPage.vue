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
    <!-- Last name -->
	<v-col cols="4">
		<v-text-field 
			label="Last Name" 
			id="lastName" 
			required
			:rules="[v => !!v || 'Last name field is required']"
			v-model="patientFamilyName"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
	</v-col>
	
	<!-- First name -->
	<v-col cols="4">
        <v-text-field 
			label="First Name" 
			id="firstName" 
			required
			:rules="[v => !!v || 'First name field is required']"
			v-model="patientGivenName">
        </v-text-field>
	</v-col>
    </v-row>
	
	<!-- Date of Birth -->
    <v-row>
	<v-col cols="5">
        <v-menu
			ref="patientLookupMenu"
			v-model="patientLookupMenu"
			:close-on-content-click="false"
			transition="scale-transition"
			offset-y
			min-width="290px"
        >
		<template v-slot:activator="{ on, attrs }">
            <v-text-field
				v-model="patientDate"
				label="Date of Birth"
				prepend-icon="mdi-calendar"
				readonly
				v-bind="attrs"
				v-on="on"
            ></v-text-field>
		</template>

		<v-date-picker
            ref="picker"
            v-model="patientDate"
            :max="new Date().toISOString().substr(0, 10)"
            min="1900-01-01"
            @change="save"
		></v-date-picker>
        </v-menu>
	</v-col>
	<v-spacer></v-spacer>
    </v-row>
	
    <v-row>
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
