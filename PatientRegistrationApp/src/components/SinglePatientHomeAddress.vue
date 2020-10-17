<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<v-text-field
					id = "addr"
					required
					:rules="[v => !!v || 'Address field is required']"
					label="Home Address"
					v-model="lineAddress"
				></v-text-field>
			</v-col>

			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "city"
					required
					:rules="[v => !!v || 'City field is required']"
					label="City"
					v-model="cityAddress"
				></v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col class="d-flex" cols="4" sm="2" md="3">
				<v-select
				id = "state"
					required
					:rules="[v => !!v || 'State field is required']"
					v-model="stateAddress"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
			<v-col class="d-flex" cols="6" sm="4">
				<v-text-field
				id = "county"
					required
					:rules="[v => !!v || 'County field is required']"
                    label="County"
					v-model="districtAddress"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="4" sm="2">
				<v-select
				id = "country"
					required
					:rules="[v => !!v || 'Country field is required']"
					v-model="countryAddress"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "zipcode"
					required
					:rules="[v => !!v || 'Zipcode field is required']"
					label="Zipcode"
					v-model="postalCode"
				></v-text-field>
			</v-col>
		</v-row>	
		<v-row align="center" justify="center">
			<v-col cols="12" sm="6" md="3">
				<v-checkbox
					v-model="checkbox"
					label="I have no home address"
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
		'Alabama', 'Alaska', 'American Samoa', 'Arizona',
		'Arkansas', 'California', 'Colorado', 'Connecticut',
		'Delaware', 'District of Columbia', 'Federated States of Micronesia',
		'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho',
		'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
		'Louisiana', 'Maine', 'Marshall Islands', 'Maryland',
		'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
		'Missouri', 'Montana', 'Nebraska', 'Nevada',
		'New Hampshire', 'New Jersey', 'New Mexico', 'New York',
		'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio',
		'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico',
		'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee',
		'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia',
		'Washington', 'West Virginia', 'Wisconsin', 'Wyoming',
		],
			country: ['United States'],
			lineAddress: '',
			cityAddress: '',
			districtAddress: '',
			stateAddress: '',
			countryAddress: '',
			postalCode: ''
		}
		
	},
	methods: {
		sendHomeAddressInfoToReviewPage()
		{
		const homeAddressPayload = {
			lineAddress: this.lineAddress,
			cityAddress: this.cityAddress,
			districtAddress: this.districtAddress,
			stateAddress: this.stateAddress,
			countryAddress: this.countryAddress,
			postalCode: this.postalCode
		}
		EventBus.$emit('DATA_ADDRESS_INFO_PUBLISHED', homeAddressPayload)
		},
		verifyFormContents()
		{
			//add logic to check form contents
			var valid = true
			var message = "Woops! You need to enter the following fields:"
			
			
			if(this.lineAddress == "") {
				message += " *address"
				valid = false
			}
			
			
			if(this.cityAddress == "") {
				message += " *city"
				valid = false
			}
				
			
			if(this.stateAddress == "") {
				message += " *state"
				valid = false
			}
				
			
			if(this.districtAddress == "") {
				message += " *county"
				valid = false
			}
				
			
			if(this.countryAddress == "") {
				message += " *country"
				valid = false
			}
				
			
			if(this.postalCode == "") {
				message += " *zipcode"
				valid = false
			}

			if (valid == false) {
				alert(message)
				return false
			}
			
			this.sendHomeAddressInfoToReviewPage();
			return true;
		}
	},
  }
</script>
