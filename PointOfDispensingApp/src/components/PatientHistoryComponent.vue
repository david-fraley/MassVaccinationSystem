<template>
  <v-container>
    <v-row>
      <v-col cols="12">
		<template>
		<v-data-table
      @click:row="rowClick"
      item-key="id"
      single-select
			:headers="headers"
			:items="patientHistoryTable"
			class="elevation-1"
			:footer-props="{
				'items-per-page-options':[4]
			}"
			:items-per-page="4"
    ></v-data-table>
		</template>
		
      </v-col>
    </v-row>

  </v-container>
</template>

<script>
  export default {
    name: 'PatientHistoryComponent',
    methods: 
    {
	rowClick: function (item, row) {      
        row.select(true);
		},
	calculatedDaysSinceDose(firstDoseDateTime) {
		// if (immunizationTimeStamp !== '' ) //checks to see if there is a time stamp
		console.log((firstDoseDateTime));
		
		this.dateFunct = new Date();

		this.now = this.dateFunct;
		this.firstDose = new Date(firstDoseDateTime);

		this.numDaysSinceLastVaccination = Math.ceil((this.now-this.firstDose) / (1000*60*60*24));
		console.log(this.numDaysSinceLastVaccination);

		// else numDays = 'N/A'

		return this.numDaysSinceLastVaccination.toString();
	},
    },
    components: 
    {
    },
    data () {
      return {
		doseNum: '',
		tradeName: '',
		doseQty: '',
		timeAdministered: '',
		numDaysSinceLastVaccination: '',
		practitionerName: '',
		adverseEffects: '',
		selectedId: -1,
		daysSinceDose: '',
		
		headers: [
		{
            text: 'Dose Num',
            align: 'start',
            sortable: false,
            value: 'doseNum',
          },
			{ text: 'Trade Name', value: 'tradeName' },
			{ text: 'Dose Qty.', value: 'doseQty' },
			{ text: 'Time Administered', value: 'timeAdministered' },
			{ text: 'Days Since Last Vaccination', value: 'numDaysSinceLastVaccination' },
			{ text: 'Practitioner', value: 'practitionerName' },
			{ text: 'Adverse Effects', value: 'adverseEffects' },
			],
		patientHistoryTable: [
			{
        id: 1,
				doseNum: '1',
				tradeName: 'VaxMan Inc.',
				doseQty: '0.5 mL',
				timeAdministered: '2020-08-16 16:22:25',
				numDaysSinceLastVaccination: this.calculatedDaysSinceDose(this.timeAdministered = '2020-09-22 16:22:25'),
				practitionerName: 'Doogie Howser',
				adverseEffects: 'N/A'
			},	
				],
      }
    },
  }
</script>
