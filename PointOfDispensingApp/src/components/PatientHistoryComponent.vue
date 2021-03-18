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
      const immunizations = this.$store.state.patientHistory;

      for (const record of immunizations) {
        record.daysSinceDose = this.calculatedDaysSinceDose(record.occurrence);
      }

      return immunizations;
		}
	},
    methods: 
    {
	rowClick: function (item, row) {      
        row.select(true);
		},
	calculatedDaysSinceDose(doseDateTime) {
    const now = new Date();
    const dateTime = new Date(doseDateTime);

    const milliPerDay = 1000*60*60*24;
    const numDaysSinceLastVaccination = Math.round((now-dateTime) / milliPerDay);

		return numDaysSinceLastVaccination.toString();
	},
    },
    data () {
      return {
		
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
			{ text: 'Days Since Last Vaccination', value: 'daysSinceDose' },
			{ text: 'Practitioner', value: 'performer' },
			],
      }
    },
  }
</script>
