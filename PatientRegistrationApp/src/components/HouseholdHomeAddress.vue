<template>
	<v-container fluid>
	<v-row>
     <v-radio-group
        required
        :rules="[v => !!v || 'This field is required']"
        v-model="homeAddressAvailableRadioButtons"
      >
        <v-col align="right" cols="12">
          <v-radio label="I have a home address" value="yes" @change="HomeAddressAvailable()"></v-radio>
          <v-radio label="I do not have a home address" value="no" @change="HomeAddressNotAvailable()"></v-radio>
        </v-col>
      </v-radio-group>
    </v-row>
		<v-row>
			<v-col cols="12" sm="12" md="12">
				<v-text-field
					id = "addr"
					required
					class="required"
					:rules="[v => !!v || 'Address field is required']"
					label="Home Address"
					v-model="householdLineAddress"
					v-show="homeAddressAvailable"
					prepend-icon="mdi-menu-right"
				></v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "city"
					required
					class="required"
					:rules="[v => !!v || 'City field is required']"
					label="City"
					v-model="householdCityAddress"
					v-show="homeAddressAvailable"
					prepend-icon="mdi-menu-right"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="1" sm="1" md="1">
				<v-select
				id = "state"
					required
					class="required"
					:rules="[v => !!v || 'State field is required']"
					v-model="householdStateAddress"
					v-show="homeAddressAvailable"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
			<v-col class="d-flex" cols="3" sm="3">
				<v-text-field
				id = "county"
					required
					class="required"
					:rules="[v => !!v || 'County field is required']"
                    label="County"
					v-model="householdDistrictAddress"
					v-show="homeAddressAvailable"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="2" sm="2">
				<v-select
				id = "country"
					required
					class="required"
					:rules="[v => !!v || 'Country field is required']"
					v-model="householdCountryAddress"
					v-show="homeAddressAvailable"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			<v-col cols="3" sm="3" md="3">
				<v-text-field
					id = "zipcode"
					required
					class="required"
					:rules="[v => !!v || 'Zipcode field is required']"
					label="Zipcode"
					v-model="householdPostalCode"
					v-show="homeAddressAvailable"
				></v-text-field>
			</v-col>
		</v-row>	
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

  export default {
	data () {
		return {
			state:[
		'AL', 'AK', 'AS', 'AZ',
		'AR', 'CA', 'CO', 'CT',
		'DE', 'DC', 'FM',
		'FL', 'GA', 'GU', 'HI', 'ID',
		'IL', 'IN', 'IA', 'KS', 'KY',
		'LA', 'ME', 'MH', 'MD',
		'MA', 'MI', 'MN', 'MS',
		'MO', 'MT', 'NE', 'NV',
		'NH', 'NJ', 'NM', 'NY',
		'NC', 'ND', 'MP', 'OH',
		'OK', 'OR', 'PW', 'PA', 'PR',
		'RI', 'SC', 'SD', 'TN',
		'TX', 'UT', 'VT', 'VI', 'VA',
		'WA', 'WV', 'WI', 'WY',
		],
			country: ['USA'],
			householdLineAddress: '',
			householdCityAddress: '',
			householdDistrictAddress: '',
			householdStateAddress: '',
			householdCountryAddress: 'USA',
			householdPostalCode: '',
			homeAddressAvailable: true,
			homeAddressAvailableRadioButtons: 'yes'
		}
		
	},
	methods: {
		sendHouseholdHomeAddressInfoToReviewPage()
		{
			const householdHomeAddressPayload = {
				householdLineAddress: this.householdLineAddress,
				householdCityAddress: this.householdCityAddress,
				householdDistrictAddress: this.householdDistrictAddress,
				householdStateAddress: this.householdStateAddress,
				householdCountryAddress: this.householdCountryAddress,
				householdPostalCode: this.householdPostalCode
			}
			EventBus.$emit('DATA_HOUSEHOLD_ADDRESS_INFO_PUBLISHED', householdHomeAddressPayload)
		},
		verifyFormContents()
		{
			//add logic to check form contents
			var valid = true
			var message = "Woops! You need to enter the following field(s):"
			
		if(this.homeAddressAvailable)
		{	
			if(this.householdLineAddress == "") 
			{
				message += " Address"
				valid = false
			}
			
			
			if(this.householdCityAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " City"
				valid = false
			}
				
			
			if(this.householdStateAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " State"
				valid = false
			}
				
			
			if(this.householdDistrictAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " County"
				valid = false
			}
				
			
			if(this.householdCountryAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " Country"
				valid = false
			}
				
			
			if(this.householdPostalCode == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " Zipcode"
				valid = false
			}
		}
			if (valid == false) 
			{
				alert(message)
				return false
			}
			
			this.sendHouseholdHomeAddressInfoToReviewPage();
			return true;
		},
	HomeAddressAvailable()
    {
	this.homeAddressAvailable = true
	this.householdLineAddress=""
	this.householdCityAddress=""
	this.householdStateAddress=""
	this.householdDistrictAddress=""
	this.householdPostalCode=""
    },
    HomeAddressNotAvailable()
    {
	this.homeAddressAvailable = false
	this.householdLineAddress="Not Available"
	this.householdCityAddress="Not Available"
	this.householdStateAddress="Not Available"
	this.householdDistrictAddress="Not Available"
	this.householdPostalCode="Not Available"
    },
	},
  }
</script>

<style lang="css" scoped>
.required:before{
	content:"*";
	color: red;
	font-weight:bold;
}
</style>

