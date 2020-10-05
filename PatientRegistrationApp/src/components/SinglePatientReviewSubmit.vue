
<template>
	<v-container fluid>
	
		<v-row
			align="center"
			justify="center"
		>
<v-heading> Please review all entries before submitting the registration! </v-heading>
</v-row>
<v-row>
	<v-col
        cols="12" 
        sm="6"
        md="3">
			<v-h2> Personal information</v-h2>
	</v-col>
</v-row>

<v-row>
<v-col
		cols="4"
		sm="3">
			<v-img
			style="float:left"		
			max-height="100"
			max-width="100" 
			src="../assets/blankPicture.png"></v-img>
 </v-col>  
	
	
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="Smith"
			label="Last Name"
			disabled></v-text-field>
	</v-col>
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="John Junior"
			label="First Name"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="01/01/1980"
			label="DOB"
			disabled></v-text-field>
	</v-col>



	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="Male"
			label="Gender ID"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="---"
			label="Race"
			disabled></v-text-field>
	</v-col>


	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="---"
			label="Ethnicity"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>
	<v-col
        cols="12"
        sm="6"
        md="3">
			<v-h3> Home Address </v-h3>
	</v-col>
</v-row>

<v-row>	
	<v-col
		cols="8"
		sm="4">
		
			<v-text-field
			dense
			value="1234 Right Lane"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>	
	<v-col
		cols="12"
		sm="6">
		
			<v-text-field
			dense
			value="Hilltop, Waukesha County, WI, USA, 53444"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>
	<v-col
        cols="12"
        sm="6"
        md="3">
			<v-h4> Contact Information</v-h4>
	</v-col>
</v-row>

<v-row>	
	<v-col cols="4">
		<v-subheader> Phone: </v-subheader>
	</v-col>
	<v-col
		cols="12"
		sm="6">
		
			<v-text-field
			dense
			value="(414) 227-0011 (cell)"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>	
	<v-col cols="4">
		<v-subheader> E-mail: </v-subheader>
	</v-col>
	<v-col
		cols="12"
		sm="6">
		
			<v-text-field
			dense
			value="johnosmith@gmail.com"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>	
	<v-col cols="6">
		<v-subheader>Preferred Method of Contact: </v-subheader>
	</v-col>
	<v-col
		cols="6"
		sm="4">
		
			<v-text-field
			dense
			value="E-mail"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>	
	<v-col cols="4">
		<v-subheader> Follow-up Approval: </v-subheader>
	</v-col>
	<v-col
		cols="12"
		sm="6">
		
			<v-text-field
			dense
			value="yes"
			disabled></v-text-field>
	</v-col>
</v-row>


<v-row>
	<v-col
        cols="12"
        sm="6"
        md="3">
			<v-h5> Emergency Contact </v-h5>
	</v-col>
</v-row>

<v-row>	
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="Jones"
			label="Last Name"
			disabled></v-text-field>
	</v-col>
	<v-col
		cols="6"
		sm="3">
		
			<v-text-field
			dense
			value="Mary"
			label="First Name"
			disabled></v-text-field>
	</v-col>
</v-row>

<v-row>	
	<v-col cols="4">
		<v-subheader> Phone: </v-subheader>
	</v-col>
	<v-col
		cols="12"
		sm="6">
		
			<v-text-field
			dense
			value="(414) 227-0011 (cell)"
			disabled></v-text-field>
	</v-col>
</v-row>


	</v-container>
</template>
 
<script>
	export default {
	data () {
		return {
			

		}
}
}
</script>

