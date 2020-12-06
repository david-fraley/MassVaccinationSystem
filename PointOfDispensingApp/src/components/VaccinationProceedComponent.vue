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
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text"><span style="color:red">*</span>Lot Number</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense
              value="####"
              id="LotNumber" 
              required
              :rules="[v => !!v || 'Lot Number field is required']"
              v-model="LotNumber"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text"><span style="color:red">*</span>Expiration Date</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense
              value="MM/DD/YYYY"
              id="ExpDate" 
              required
              :rules="[v => !!v || 'Expiration Date field is required']"
              v-model="ExpDate"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text"><span style="color:red">*</span>Manufacturer</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense
              value="Pfizer"
              id="Manufacturer" 
              required
              :rules="[v => !!v || 'Manufacturer field is required']"
              v-model="Manufacturer"
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
            <div class="secondary--text"><span style="color:red">*</span>Dose Quantity</div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="doseQuantityOptions"
              outlined
              dense
              required
              :rules="[v => !!v || 'Dose Quantity field is required']"
              v-model="DoseQty"
            ></v-select>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text"><span style="color:red">*</span>Dose Number</div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="doseNumberOptions"
              outlined
              dense
              v-model="DoseNumber"
              required
              :rules="[v => !!v || 'Dose Number field is required']"
              id="DoseNumber"
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>    
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <p> </p> <!--blank row for spacing-->
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn color="accent">
          Vaccine administered
        </v-btn>
      </v-col>
    </v-row>
	
    <v-row>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">Vaccination Status</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense filled readonly
              value="completed"
              id="vaccinationStatus"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">Healthcare Practitioner</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense filled readonly
              value="Doogie Howser"
              id="healthPractitioner" 
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text"><span style="color:red">*</span>Site of Vaccination</div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="vaccinationSiteOptions"
              outlined
              dense
              required
              :rules="[v => !!v || 'Site of Vaccination field is required']"
              v-model="siteOfVaccination"
            ></v-select>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">Route</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense filled readonly
              value="Injection"
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
            <div class="secondary--text">Date/Time</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense filled readonly
              value="04/21/2021  11:13:27"
              id="vaccinationStatus" 
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
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
	<template> 
		<div class="text-center">
		<v-dialog
			v-model="dialog"
			width="500"
		>
		<template v-slot:activator="{ on, attrs }">
      <v-col cols="6">
        <v-btn block color="accent" v-bind="attrs" v-on="on">
          Submit vaccination record
	</v-btn>
	</v-col>
	</template>
	
	<v-card>
		<v-card-title class="headline grey lighten-2 justify-center"> 
		Are you sure you want to submit?
		</v-card-title>
		
		<v-card-actions>
		<v-btn	
			color="primary"
			text
			@click="dialog = false">
			Back
		</v-btn>
		<v-spacer></v-spacer>
		<v-btn	
			color="primary"
			text
			@click="submitVaccinationRecord()">
			Submit
		</v-btn>
		</v-card-actions>
		</v-card>
		</v-dialog>
		</div>
		</template>

    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'VaccinationProceedComponent',
    methods: 
    {
      submitVaccinationRecord() {
        this.dialog = false;
        this.$router.push("Discharge")
      }
    },
    components: 
    {
    },
    data () {
      return {
        dialog: false,
        doseNumberOptions: ['1', '2'],
        doseQuantityOptions: ['0.1 mL', '0.2 mL', '0.5 mL', '1.0 mL'],
        vaccinationSiteOptions: ['Left arm', 'Right arm'],
      }
    }
  }
</script>