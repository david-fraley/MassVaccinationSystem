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
          <v-col cols="4">
            <div class="secondary--text">Zip Code</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="PostalCode"
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
		<template>
		<v-data-table
      @click:row="rowClick"
      item-key="id"
      single-select
			:headers="headers"
			:items="patientLookupTable"
			class="elevation-1"
			:footer-props="{
				'items-per-page-options':[5]
			}"
			:items-per-page="5"
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
			{ text: 'Zipcode', value: 'postalCode' },
			],
		patientLookupTable: [
			{
        id: 1,
        familyName: 'Johnson',
				givenName: 'Jacob',
				DOB: '10/31/1990',
				lineAddress: '6585 Lake St',
				city: 'Glendale',
				postalCode: '53209',
			},
			
			{
        id: 2,
        familyName: 'Johnson',
				givenName: 'Larry',
				DOB: '04/15/1972',
				lineAddress: '721 Hillcrest Dr',
				city: 'Bayside',
				postalCode: '53217',
			},
			
			{
        id: 3,
				familyName: 'Johnson',
				givenName: 'Jennifer',
				DOB: '01/23/1972',
				lineAddress: '1234 Right Lane',
				city: 'New Berlin',
				postalCode: '53146',
			},
			
			{
        id: 4,
				familyName: 'Johnson',
				givenName: 'Joan',
				DOB: '02/01/1960',
				lineAddress: '2 Valley Rd',
				city: 'Germantown',
				postalCode: '',
			},
			
			{
        id: 5,
				familyName: 'Johnson',
				givenName: 'Isaac',
				DOB: '12/21/2000',
				lineAddress: '3823 Pine St',
				city: 'Brown Deer',
				postalCode: '53223',
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
