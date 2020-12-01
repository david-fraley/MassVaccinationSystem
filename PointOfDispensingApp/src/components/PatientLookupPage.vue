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
		<template>
		<v-data-table
      @click:row="rowClick"
      item-key="id"
      single-select
			:headers="headers"
			:items="patientLookupTable"
			:items-per-page="5"
			class="elevation-1"
    ></v-data-table>
		</template>
		
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent" @click="retrievePatientRecord()">
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
      rowClick: function (item, row) {      
        row.select(true);
        this.selectedId=item.id
        this.selectedFamilyName=item.familyName
        this.selectedGivenName=item.givenName
        this.selectedDOB=item.DOB
      },
      retrievePatientRecord()
      {
        alert(this.selectedFamilyName + ', ' + this.selectedGivenName + ', ' + this.selectedDOB)
      }
    },
    components: 
    {
    },
    data () {
	return {
		patientFamilyName: '',
		patientGivenName: '',
    patientDate: '',
    selectedId: -1,
    selectedFamilyName: '',
    selectedGivenName: '',
    selectedDOB: '',

		headers: [
          {
            text: 'Last Name',
            align: 'start',
            sortable: false,
            value: 'familyName',
          },
          { text: 'First Name', value: 'givenName' },
          { text: 'DOB', value: 'DOB' },
          { text: 'Address', value: 'lineAddress' },
          { text: 'City', value: 'city' },
			],
		patientLookupTable: [
			{
        id: 1,
        familyName: 'Johnson',
				givenName: 'Jacob',
				DOB: '10/31/1990',
				lineAddress: '6585 Lake St',
				city: 'Glendale',
			},
			
			{
        id: 2,
        familyName: 'Johnson',
				givenName: 'Larry',
				DOB: '04/15/1972',
				lineAddress: '721 Hillcrest Dr',
				city: 'Bayside',
			},
			
			{
        id: 3,
				familyName: 'Johnson',
				givenName: 'Jennifer',
				DOB: '01/23/1972',
				lineAddress: '1234 Right Lane',
				city: 'New Berlin',
			},
			
			{
        id: 4,
				familyName: 'Johnson',
				givenName: 'Joan',
				DOB: '02/01/1960',
				lineAddress: '2 Valley Rd',
				city: 'Germantown',
			},
			
			{
        id: 5,
				familyName: 'Johnson',
				givenName: 'Isaac',
				DOB: '12/21/2000',
				lineAddress: '3823 Pine St',
				city: 'Brown Deer',
			},
		],
    }
	},
	}
</script>
<style>
  tr.v-data-table__selected {
    background: #1976D2 !important;
    color: #FFFFFF
  }
</style>
