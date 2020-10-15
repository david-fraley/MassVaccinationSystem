<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<v-text-field
					id = "addr"
					:rules="['Required']"
					label="Home Address"
					v-model="lineAddress"
				></v-text-field>
			</v-col>

			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "city"
					:rules="['Required']"
					label="City"
					v-model="cityAddress"
				></v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col class="d-flex" cols="4" sm="2" md="3">
				<v-select
				id = "state"
					:rules="['Required']"
					v-model="stateAddress"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
			<v-col class="d-flex" cols="6" sm="4">
				<v-text-field
				id = "county"
					:rules="['Required']"
                    label="County"
					v-model="districtAddress"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="4" sm="2">
				<v-select
				id = "country"
					:rules="['Required']"
					v-model="countryAddress"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "zipcode"
					:rules="['Required']"
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
			alert('verifyformcontents')
			//add logic to check form contents
			var valid = true
			var message = "Woops! You need to enter the following fields:"
			
			//var a = document.getElementById("addr")
			//a.required = true
			//this.lineAddress.required = true  <-- keeping this line doesn't work for some reason
			//if (a.value == "") {
			if(this.lineAddress == "") {
				message += " *address"
				valid = false
			}
			
			//var b = document.getElementById("city")
			//b.required = true
			//this.cityAddress.required = true
			//if (b.value == "") {
			if(this.cityAddress == "") {
				message += " *city"
				valid = false
			}
				
			//var c = document.getElementById("state")
			//c.required = true
			//this.stateAddress.required = true
			//if (c.value == "state") {
			if(this.stateAddress == "") {
				message += " *state"
				valid = false
			}
				
			//var d = document.getElementById("county")
			//d.required = true
			//this.districtAddress.required = true
			//if (d.value == "") {
			if(this.districtAddress == "") {
				message += " *county"
				valid = false
			}
				
			//var e = document.getElementById("country")
			//e.required = true
			//this.countryAddress.required = true
			//if (e.value == "country") {
			if(this.countryAddress == "") {
				message += " *country"
				valid = false
			}
				
			//var f = document.getElementById("zipcode")
			//f.required = true
			//this.postalCode.required = true
			//if (f.value == "") {
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
