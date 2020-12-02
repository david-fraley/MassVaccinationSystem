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
					:rules="[v => !!v || 'Address field is required']"
					v-model="lineAddress"
					v-show="homeAddressAvailable"
					prepend-icon="mdi-menu-right">
						<template #label>
						<span class="red--text"><strong>* </strong></span>Home Address
						</template>
				</v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					id = "city"
					required
					:rules="[v => !!v || 'City field is required']"
					v-model="cityAddress"
					v-show="homeAddressAvailable"
					prepend-icon="mdi-menu-right">
						<template #label>
						<span class="red--text"><strong>* </strong></span>City
						</template>
				</v-text-field>
			</v-col>
				<v-col class="d-flex" cols="1" sm="1" md="1">
				<v-select
				id = "state"
					required
					:rules="[v => !!v || 'State field is required']"
					v-model="stateAddress"
					v-show="homeAddressAvailable"
                    :items="state">
						<template #label>
						<span class="red--text"><strong>* </strong></span>State
						</template>
				</v-select>
			</v-col>
			<v-col class="d-flex" cols="3" sm="3">
				<v-text-field
				id = "county"
					required
					:rules="[v => !!v || 'County field is required']"
					v-model="districtAddress"
					v-show="homeAddressAvailable">
						<template #label>
						<span class="red--text"><strong>* </strong></span>County
						</template>
				</v-text-field>
			</v-col>
			<v-col class="d-flex" cols="2" sm="2">
				<v-select
				id = "country"
					required
					:rules="[v => !!v || 'Country field is required']"
					v-model="countryAddress"
					v-show="homeAddressAvailable"
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
					v-model="postalCode"
					v-show="homeAddressAvailable">
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
			country: ['USA'],
			lineAddress: '',
			cityAddress: '',
			districtAddress: '',
			stateAddress: '',
			countryAddress: 'USA',
			postalCode: '',
			homeAddressAvailable: true,
			homeAddressAvailableRadioButtons: 'yes'
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
			var message = "Woops! You need to enter the following field(s):"
			
		if(this.homeAddressAvailable)
		{	
			if(this.lineAddress == "") 
			{
				message += " Address"
				valid = false
			}
			
			
			if(this.cityAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " City"
				valid = false
			}
				
			
			if(this.stateAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " State"
				valid = false
			}
				
			
			if(this.districtAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " County"
				valid = false
			}
				
			
			if(this.countryAddress == "") 
			{
			if(!valid)
				{
				message +=","
				}
				message += " Country"
				valid = false
			}
				
			
			if(this.postalCode == "") 
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
			
			this.sendHomeAddressInfoToReviewPage();
			return true;
		},
	HomeAddressAvailable()
    {
	this.homeAddressAvailable = true
	this.lineAddress=""
	this.cityAddress=""
	this.stateAddress=""
	this.districtAddress=""
	this.postalCode=""
    },
    HomeAddressNotAvailable()
    {
	this.homeAddressAvailable = false
	this.lineAddress="Not Available"
	this.cityAddress="Not Available"
	this.stateAddress="Not Available"
	this.districtAddress="Not Available"
	this.postalCode="Not Available"
    },
	},
}
</script>
