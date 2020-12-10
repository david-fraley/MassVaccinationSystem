<template>
	<v-container fluid>
	<v-row>
		<v-col cols="5" sm="5" md="5">
			<v-select
				required
				:rules="[v => !!v || 'Address Type is required']"
				v-model="addressType"
				:items="addressTypeOptions"
				prepend-icon="mdi-menu-right"
			>
				<template #label>
					<span class="red--text"><strong>* </strong></span>Address Type
				</template>
			</v-select>
		</v-col>
	</v-row>
	<v-row>
		<v-col cols="12" sm="12" md="12">
			<v-text-field
				required
				:rules="[v => !!v || 'Address field is required']"
				v-model="householdLineAddress1"
				prepend-icon="mdi-menu-right">
					<template #label>
					<span class="red--text"><strong>* </strong></span>Address Line 1
					</template>
			</v-text-field>
		</v-col>
	</v-row>
	<v-row>
		<v-col cols="12" sm="12" md="12">
			<v-text-field
				v-model="householdLineAddress2"
				prepend-icon="mdi-menu-right"
				label="Address Line 2">
			</v-text-field>
		</v-col>
	</v-row>
		<v-row>
			<v-col cols="5" sm="5" md="5">
				<v-text-field
					id = "city"
					required
					:rules="[v => !!v || 'City field is required']"
					v-model="householdCityAddress"
					prepend-icon="mdi-menu-right">
						<template #label>
						<span class="red--text"><strong>* </strong></span>City
						</template>
				</v-text-field>
			</v-col>
			<v-col class="d-flex" cols="2" sm="2" md="2">
				<v-select
				id = "state"
					required
					:rules="[v => !!v || 'State field is required']"
					v-model="householdStateAddress"
					:items="state">
						<template #label>
						<span class="red--text"><strong>* </strong></span>State
						</template>
				</v-select>
			</v-col>
			<v-col class="d-flex" cols="2" sm="2" md="2">
				<v-select
				id = "country"
					required
					:rules="[v => !!v || 'Country field is required']"
					v-model="householdCountryAddress"
					:items="country">
						<template #label>
						<span class="red--text"><strong>* </strong></span>Country
						</template>
				</v-select>
			</v-col>
			<v-col cols="3" sm="3" md="3">
				<v-text-field
					id = "zipcode"
					required
					:rules="postalCodeRules"
					v-model="householdPostalCode">
						<template #label>
						<span class="red--text"><strong>* </strong></span>Zipcode
						</template>
				</v-text-field>
			</v-col>
		</v-row>	
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

  export default {
	data () {
		return {
			postalCodeRules: [
        (v) => !!v || "Zip code is required",
			(v) =>
			/(^\d{5}$)|(^\d{5}-\d{4}$)/.test(v) ||
			"Zip code must be in format of ##### or #####-####",
		],
		
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
			addressTypeOptions: ["Home", "Business", "Temporary"],
			country: ['USA'],
			addressType: '',
			householdLineAddress1: '',
			householdLineAddress2: '',
			householdCityAddress: '',
			householdStateAddress: '',
			householdCountryAddress: 'USA',
			householdPostalCode: '',
		}
		
	},
	methods: {
		sendHouseholdHomeAddressInfoToReviewPage()
		{
			const householdHomeAddressPayload = {
				addressType: this.addressType,
				householdLineAddress1: this.householdLineAddress1,
				householdLineAddress2: this.householdLineAddress2,
				householdCityAddress: this.householdCityAddress,
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

			if(this.addressType == "") 
			{
				message += " Address Type"
				valid = false
			}	
		
			if(this.householdLineAddress1 == "") 
			{
				if(!valid){
					message +=","
				}
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

			if (valid == false) 
			{
				alert(message)
				return false
			}
			
			this.sendHouseholdHomeAddressInfoToReviewPage();
			return true;
		},
	},
  }
</script>


