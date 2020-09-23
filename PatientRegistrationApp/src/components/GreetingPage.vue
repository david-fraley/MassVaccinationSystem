
<template>
	<v-container fluid>
	
		<v-row
			align="center"
			justify="center"
		>
		
			<v-col class="d-flex" cols="10" sm="10">
				<v-select
					:items="items"
					label="Please select your preferred language"
					dense
				></v-select>
			</v-col>
		</v-row>
     
		<v-row
			align="center"
			justify="center"
		>
		
			<v-col 
				class="d-flex" 
				cols="12" 
				sm="6"
			>
				
				<v-tooltip top>
					<template v-slot:activator="{on, attrs}">
						<v-btn 
							class="ma-2"
							outlined 
							rounded 
							x-large 
							color="secondary"
							v-bind="attrs"
							v-on="on"
						>
							<v-icon left>mdi-account</v-icon>
							Register myself
						</v-btn>
					</template>
					<span>Register yourself!</span>
				</v-tooltip>
				
			</v-col>

			<v-col 
				class="d-flex" 
				cols="12" 
				sm="6"
			>
				<v-tooltip top>
					<template v-slot:activator="{on, attrs}">
						<v-btn 
							class="ma-2"
							outlined 
							rounded 
							x-large 
							color="secondary"
							v-bind="attrs"
							v-on="on"
						>
							<v-icon left>mdi-account-group</v-icon>
							Register my household
						</v-btn>
					</template>
					<span>Household definition...</span>
				</v-tooltip>
				
			</v-col>
		</v-row>
	</v-container>
</template>

<script>

  export default {
    name: 'GreetingPage',
	data: () => ({
	items: ['English','Spanish'],
	}),
  }
</script>
