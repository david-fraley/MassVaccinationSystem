<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" sm="12" md="12">
				<v-text-field
					id = "addr"
					required
					:rules="[v => !!v || 'Address field is required']"
					label="Home Address"
					v-model="householdLineAddress"
				></v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "city"
					required
					:rules="[v => !!v || 'City field is required']"
					label="City"
					v-model="householdCityAddress"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="1" sm="1" md="1">
				<v-select
				id = "state"
					required
					:rules="[v => !!v || 'State field is required']"
					v-model="householdStateAddress"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
			<v-col class="d-flex" cols="3" sm="3">
				<v-text-field
				id = "county"
					required
					:rules="[v => !!v || 'County field is required']"
                    label="County"
					v-model="householdDistrictAddress"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="2" sm="2">
				<v-select
				id = "country"
					required
					:rules="[v => !!v || 'Country field is required']"
					v-model="householdCountryAddress"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			<v-col cols="3" sm="3" md="3">
				<v-text-field
					id = "zipcode"
					required
					:rules="[v => !!v || 'Zipcode field is required']"
					label="Zipcode"
					v-model="householdPostalCode"
				></v-text-field>
			</v-col>
		</v-row>	
		<v-row align="center" justify="center">
			<v-col cols="12" sm="6" md="3">
				<v-checkbox
					v-model="checkbox"
					label="I/We have no home address"
				></v-checkbox>
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
			country: ['United States'],
			householdLineAddress: '',
			householdCityAddress: '',
			householdDistrictAddress: '',
			householdStateAddress: '',
			householdCountryAddress: '',
			householdPostalCode: ''
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
			var message = "Woops! You need to enter the following fields:"
			
			
			if(this.householdLineAddress == "") {
				message += " *address"
				valid = false
			}
			
			
			if(this.householdCityAddress == "") {
				message += " *city"
				valid = false
			}
				
			
			if(this.householdStateAddress == "") {
				message += " *state"
				valid = false
			}
				
			
			if(this.householdDistrictAddress == "") {
				message += " *county"
				valid = false
			}
				
			
			if(this.householdCountryAddress == "") {
				message += " *country"
				valid = false
			}
				
			
			if(this.householdPostalCode == "") {
				message += " *zipcode"
				valid = false
			}

			if (valid == false) {
				alert(message)
				return false
			}
			
			this.sendHouseholdHomeAddressInfoToReviewPage();
			return true;
		}
	},
  }
</script>
