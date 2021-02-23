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
			:items="immunizations"
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
	computed: {
		immunizations(){
			return this.$store.state.patientHistory
		}
	},
    methods: 
    {
	rowClick: function (item, row) {      
        row.select(true);
		},
	calculatedDaysSinceDose(firstDoseDateTime) {
		this.dateFunct = new Date();

		this.now = this.dateFunct;
		this.firstDose = new Date(firstDoseDateTime);

		this.numDaysSinceLastVaccination = Math.ceil((this.now-this.firstDose) / (1000*60*60*24));

		return this.numDaysSinceLastVaccination.toString();
	},
    },
    components: 
    {
    },
    data () {
      return {
		numDaysSinceLastVaccination: '',
		selectedId: -1,
		daysSinceDose: '',
		
		headers: [
		{
            text: 'Dose Num',
            align: 'start',
            sortable: false,
            value: 'doseNumber',
          },
			{ text: 'Trade Name', value: 'manufacturer' },
			{ text: 'Dose Qty.', value: 'doseQuantity' },
			{ text: 'Time Administered', value: 'occurrence' },
			{ text: 'Days Since Last Vaccination', value: 'numDaysSinceLastVaccination' },
			{ text: 'Practitioner', value: 'performer' },
			{ text: 'Adverse Effects', value: '' },
			],
      }
    },
  }
</script>
