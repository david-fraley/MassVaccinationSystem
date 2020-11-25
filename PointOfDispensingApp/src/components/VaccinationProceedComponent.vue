<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Scan vaccine barcode
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
		<v-col cols="6">
		<v-text-field 
			label="LOT Number" 
			id="LOTNumber" 
			required
			class="required"
			:rules="[v => !!v || 'LOT Number field is required']"
			v-model="LOTNumber"
			prepend-icon="mdi-menu-right"
        ></v-text-field> </v-col>
		<v-col cols="5">
		<v-select
			v-model="DoseQty"
			class="required"
			:items="DoseQty"
			label="Dose qty."
        ></v-select></v-col>
	</v-row>
		
		
	<v-row>
	<v-col cols="6">
		<v-text-field 
			label="Expiration Date" 
			id="ExpDate" 
			required
			:rules="[v => !!v || 'Expiration Date field is required']"
			v-model="ExpDate"
			prepend-icon="mdi-menu-right"
        ></v-text-field></v-col>
	<v-col cols="6">
		<v-text-field
			v-model="DoseNumber"
			required
			class="required"
			:rules="[v => !!v || 'Dose number field is required']"
			id="DoseNumber"
			label="Dose number"
        ></v-text-field></v-col>
	</v-row>
	
	<v-row>
	<v-col cols="6">
        <v-text-field 
			label="Manufacturer" 
			id="Manufacturer" 
			required
			class="required"
			:rules="[v => !!v || 'Manufacturer field is required']"
			v-model="Manufacturer"
			prepend-icon="mdi-menu-right"
        ></v-text-field></v-col>
	</v-row>

    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Vaccine administered
        </v-btn>
      </v-col>
    </v-row>
	
    <v-row>
		<v-col cols="6">
        <v-text-field 
			label="Vaccination Status" 
			id="vaccinationStatus" 
			required
			:rules="[v => !!v || 'Vaccination Status field is required']"
			v-model="vaccinationStatus"
			prepend-icon="mdi-menu-right"
        ></v-text-field></v-col>
		<v-col cols="6">
		<v-text-field 
			label="Vaccination Time Stamp" 
			id="vaccinationTimeStamp" 
			required
			:rules="[v => !!v || 'Vaccination Time Stamp field is required']"
			v-model="vaccinationTimeStamp"
        ></v-text-field></v-col>
        </v-row>
		
	<v-row>
		<v-col cols="6">
        <v-text-field 
			label="Health practitioner" 
			id="healthPractitioner" 
			required
			:rules="[v => !!v || 'Health practitioner field is required']"
			v-model="healthPractitioner"
			prepend-icon="mdi-menu-right"
        ></v-text-field></v-col>
	</v-row>
	
	<v-row>
        <v-col cols="6">
		<v-select
			v-model="siteOfVaccination"
			class="required"
			:items="siteOfVaccination"
			label="Site of vaccination"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>
	</v-row>
	
	<v-row>
        <v-col cols="6">
		<v-select
			v-model="route"
			:items="route"
			label="Route"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>
	</v-row>

    <v-row>
		<v-col cols="12">
		<v-textarea
		id="notes"
		placeholder="Notes"
		outlined
		rows="4"
		></v-textarea>
		</v-col>
    </v-row>
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent">
          Submit vaccination record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'VaccinationProceedComponent',
    methods: 
    {
    },
    components: 
    {
    },
    data () {
		return {
			DoseQty: [
			"0.1 mL",
			"0.2 mL",
			"0.5 mL",
			"1 mL",
			],
			
			siteOfVaccination: [
			"Left arm",
			"Right arm",
			],
			
			route: [
			"IDINJ (injection, intradermal)",
			"IM (injection, intramuscular)",
			"NASINHLC (inhalation, nasal) INVINJ (injection, intravenous)",
			"PO (swallow, oral)",
			"SQ (injection, subcutaneous)",
			"TRNSDERM (transdermal)",
			],
			
		}
    }
  }
</script>

<style lang="css" scoped>
.required:before{
	content:"*";
	color: red;
	font-weight:bold;
	float: left;
}
</style>