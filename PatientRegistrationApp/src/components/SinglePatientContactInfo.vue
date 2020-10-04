
<template>
	<v-container fluid>
	
			<v-row>
		<v-col
        cols="12"
        sm="6"
        md="3">
			<v-text-field
			:rules="['Required']"
            label="Primary Phone Number"
			placeholder="###-###-####"
			></v-text-field>
        </v-col>

        <v-col
          class="d-flex" 
				cols="6" 
				sm="2">
							
				<v-select
					:rules="['Required']"
					v-model="e7"
                    :items="phonetype"
                    label="Phone Type"
				></v-select>
        </v-col>
		
		<v-col
        cols="12"
        sm="6"
        md="3">
			<v-text-field
            label="Secondary Phone Number"
			placeholder="###-###-####"
			></v-text-field>
        </v-col>

        <v-col
          class="d-flex" 
				cols="4" 
				sm="2">
							
				<v-select
					v-model="e7"
                    :items="phonetype"
                    label="Phone Type"
				></v-select>
        </v-col>
</v-row>

<v-row>

		<v-col
			cols="6"
			sm="6"
			md="3"
		>
		
		<v-checkbox
			v-model="checkbox"
			label="I have no phone number"
		></v-checkbox>
		</v-col>

</v-row>

<v-row>
		<v-col
        cols="12"
        sm="12"
        md="6">
			<v-text-field
			:rules="['Required']"
            label="Primary Email Address"
			placeholder="johnsmith@email.com"
			></v-text-field>
        </v-col>

		
		<v-col
        cols="12"
        sm="12"
        md="6">
			<v-text-field
            label="Secondary Email Address"
			placeholder="johnsmith@email.com"
			></v-text-field>
        </v-col>

</v-row>
		<v-col
			cols="6"
			sm="6"
			md="3"
		>
		
		<v-checkbox
			v-model="checkbox"
			label="I have no email address"
		></v-checkbox>
		</v-col>



<v-row>
	<v-radio-group
        v-model="test"
       
      >

        <v-radio
          label="Option 1"
          value="radio-1"
        ></v-radio>
        <v-radio
          label="Option 2"
          value="radio-2"
        ></v-radio>
      </v-radio-group>
	
</v-row>

</v-container>
</template>

<script>

  export default {
    name: 'SinglePatientContactInfo',
	data () {
		return { 
			phonetype:['Cell', 'Landline', 'Other'],
			radios: 'May we contact you for a follow up vaccination?'
		}
		
	},
  }
</script>
