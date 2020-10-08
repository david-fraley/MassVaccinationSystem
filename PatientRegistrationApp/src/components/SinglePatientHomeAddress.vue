
<template>
	<v-container fluid>
		<v-row>
		<v-col
        cols="12"
        sm="6"
        md="3">
			<v-text-field
			id = "addr"
			:rules="['Required']"
            label="Home Address"
			placeholder="1234 Right Lane"
			></v-text-field>
        </v-col>

        <v-col
          cols="6"
          sm="4"
          md="3">
			<v-text-field
			id = "city"
			:rules="['Required']"
            label="City"
            placeholder="Hilltop"
          ></v-text-field>
        </v-col>
</v-row>

<v-row>
<v-col 
				class="d-flex" 
				cols="4" 
				sm="2"
				md="3">
							
				<v-select
				id = "state"
					:rules="['Required']"
					v-model="e6"
                    :items="state"
                    label="State"
				></v-select>
			</v-col>
		
<v-col 
				class="d-flex" 
				cols="6" 
				sm="4">
							
				<v-text-field
				id = "county"
					:rules="['Required']"
                    label="County"
					placeholder="Racine County"
				></v-text-field>
			</v-col>
 
 <v-col 
				class="d-flex" 
				cols="4" 
				sm="2">
							
				<v-select
				id = "country"
					:rules="['Required']"
					v-model="e7"
                    :items="country"
                    label="Country"
				></v-select>
			</v-col>
			
 <v-col
          cols="6"
          sm="4"
          md="3"
        >
			<v-text-field
			id = "zipcode"
			:rules="['Required']"
            label="Zipcode"
			placeholder="534444"
			></v-text-field>
		</v-col>
</v-row>	

		<v-row
			align="center"
			justify="center"
		>
		
		<v-col
			cols="12"
			sm="6"
			md="3"
		>
		
		<v-checkbox
			v-model="checkbox"
			label="I have no home address"
		></v-checkbox>
		</v-col>	  		
		</v-row>
		
	</v-container>
</template>

<script>

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
  }
</script>
