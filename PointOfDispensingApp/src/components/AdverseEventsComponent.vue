<template>
  <v-container>
	<v-row>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Vaccination status</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="immunizationStatus"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Date/Time Stamp</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="immunizationTimeStamp"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Manufacturer</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="manufacturer"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Lot Number</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="lotNumber"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Body Site</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="bodySite"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
		<v-col cols="2">
			<div class="font-weight-medium secondary--text">Dose Number</div>
		</v-col>
		<v-col cols="3">
			<v-text-field
				filled
				dense
				readonly
				outlined
				:value="doseNumber"
			></v-text-field>
		</v-col>
		<v-col cols="1">
		<!--space between columns-->
		</v-col>
	</v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'AdverseEventsComponent',
    methods: 
    {
    },
    computed: 
    {
		immunizationStatus() {
			return this.$store.state.immunizationResource.immunizationStatus;
		},
		immunizationTimeStamp() {
			return this.$store.state.immunizationResource.immunizationTimeStamp;
		},
		manufacturer() {
			return this.$store.state.immunizationResource.manufacturer;
		},
		lotNumber() {
			return this.$store.state.immunizationResource.lotNumber;
		},
		bodySite() {
			return this.$store.state.immunizationResource.site;
		},
		doseNumber() {
			return this.$store.state.immunizationResource.doseNumber;
		}
    },
    data () {
		return {
		reactionList: [
			"Rash",
			"Trouble breathing",
			"Fever",
			"Fainting",
			],
      }
    }
  }
</script>
