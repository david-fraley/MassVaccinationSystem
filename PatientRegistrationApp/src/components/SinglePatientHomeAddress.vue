<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<v-text-field
					:rules="['Required']"
					label="Home Address"
					v-model="streetAddr"
				></v-text-field>
			</v-col>

			<v-col cols="6" sm="4" md="3">
				<v-text-field
					:rules="['Required']"
					label="City"
					v-model="cityAddr"
				></v-text-field>
			</v-col>
		</v-row>
		<v-row>
			<v-col class="d-flex" cols="4" sm="2" md="3">
				<v-select
					:rules="['Required']"
					v-model="stateAddr"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
			<v-col class="d-flex" cols="6" sm="4">
				<v-text-field
					:rules="['Required']"
                    label="County"
					v-model="countyAddr"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="4" sm="2">
				<v-select
					:rules="['Required']"
					v-model="countryAddr"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			<v-col cols="6" sm="4" md="3">
				<v-text-field
					:rules="['Required']"
					label="Zipcode"
					v-model="zipAddr"
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
		<v-row>
			<v-col cols="12" >
				<v-btn
					large
					color="secondary"
					@click="sendHomeAddressInfoToReviewPage"
				>
					Send data
				</v-btn>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

  export default {
    name: 'SinglePatientHomeAddress',
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
		}
	},
	methods: {
		sendHomeAddressInfoToReviewPage()
		{
		const homeAddressPayload = {
			streetAddr: this.streetAddr,
			cityAddr: this.cityAddr,
			countyAddr: this.countyAddr,
			stateAddr: this.stateAddr,
			countryAddr: this.countryAddr,
			zipAddr: this.zipAddr
		}
		EventBus.$emit('DATA_ADDRESS_INFO_PUBLISHED', homeAddressPayload)
		}
	}
  }
</script>
