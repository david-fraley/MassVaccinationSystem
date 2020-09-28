
<template>
	<v-container bg fill-height grid-list-md text-xs-center>
		<v-row
			align="center"
			justify="center"
		>
		
			<v-col 
				class="d-flex" 
				cols="9" 
				sm="9">
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
				class="d-flex flex-wrap" 
				cols="9" 
				sm="9"
			>
				
				<v-tooltip top>
					<template v-slot:activator="{on, attrs}">
						<v-btn 
							class="ma-2"
							outlined 
							rounded 
							x-large 
							color="blue"
							v-bind="attrs"
							v-on="on"
						>
							<v-icon left style="color:#FDD835;">mdi-account</v-icon>
							Register myself
						</v-btn>
					</template>
					<span>Register yourself!</span>
				</v-tooltip>
				
				<v-spacer></v-spacer>
				
				<v-tooltip top>
					<template v-slot:activator="{on, attrs}">
						<v-btn 
							class="ma-2"
							outlined 
							rounded 
							x-large 
							color="blue"
							v-bind="attrs"
							v-on="on"
						>
							<v-icon left style="color:#FDD835;">mdi-account-group</v-icon>
							Register my household
						</v-btn>
					</template>
					<span>A household is composed of any people who occupy the same given housing unit. If you are planning to register your household, all members must currently be residing in the same address. If members of a household are residing in other housing units, it is necessary to register any given members individually. Max household registration of 20 registrants.</span>
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
