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
	<v-row>
		<v-col cols="6">
			<v-btn @click="calculatedDaysSinceDose()">
				calculate
				</v-btn>
		</v-col>
		<v-col cols="6">
		<v-text-field
			v-model="daysSinceDose">
		</v-text-field>
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
	calculatedDaysSinceDose() {
		console.log("hello");
		this.dateFunct = new Date();

		this.now = this.dateFunct;
		this.firstDose = new Date("2021-01-25T02:48:30.166Z");

		this.numDaysSinceLastVaccination = Math.ceil((this.now-this.firstDose) / (1000*60*60*24));
		console.log(this.numDaysSinceLastVaccination);

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
		LOT: '',
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
			{ text: 'LOT', value: 'LOT' },
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
				LOT: 'PTL16359',
				practitionerName: 'Doogie Howser',
				adverseEffects: 'N/A'
			},	
				],
      }
    },
  }
</script>
