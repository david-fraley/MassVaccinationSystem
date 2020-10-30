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
					:items="preferredLanguage"
					label="Please select your preferred language"
					required
					:rules="[v => !!v || 'Preferred language field is required']"
					dense
					v-model="preferredLanguage"
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
				
				<v-btn-toggle
					v-model="text"
					tile
					x-large
					color="primary"
					group
				>
					<v-tooltip top>
						<template v-slot:activator="{on, attrs}">
							<v-btn 
									class="ma-2"
									outlined 
									x-large 
									color="primary"
									v-bind="attrs"
									v-on="on"
									@click="singleRegistration"
							>
									<v-icon left color="secondary">mdi-account</v-icon>
									Register myself
							</v-btn>
						</template>
						<span>Register yourself!</span>
					</v-tooltip>
					<v-tooltip top>
						<template v-slot:activator="{on, attrs}">
							<v-btn 
									class="ma-2"
									outlined 
									x-large 
									color="primary"
									v-bind="attrs"
									v-on="on"
									@click="householdRegistration"
								>
									<v-icon left color="secondary">mdi-account-group</v-icon>
									Register my household
							</v-btn>
						</template>
						<span>A household is composed of any people who occupy the same given housing unit. If you are planning to register your household, all members must currently be residing in the same address. If members of a household are residing in other housing units, it is necessary to register any given members individually. Max household registration of 20 registrants.</span>
					</v-tooltip>
				</v-btn-toggle>
			</v-col>
		</v-row>
	</v-container>
</template>



<script>
import EventBus from '../eventBus'

export default {
	name: "GreetingPage",
	data: () => ({
		preferredLanguage: ['English','Spanish'],
	
	}),
      
     
 
	methods: {
  
	singleRegistration()
		{
			this.$emit("singleRegistration");
		},
		householdRegistration()
		{
			this.$emit("householdRegistration");
		},
	sendGreetingPageInfoToReviewPage()
	{
		const greetingPagePayload = {
		preferredLanguage: this.preferredLanguage,
		}
		EventBus.$emit('DATA_LANGUAGE_INFO_PUBLISHED', greetingPagePayload)
	}, 
	verifyFormContents()
    {
		//add logic to check form contents
		var valid = true
		var message = "Woops! You need to enter the following fields:"
	
			if (this.preferredLanguage == "") {
			message += "*Preferred Language"
			valid = false
			}
			if (valid == false) {
				alert(message)
				return false
			}
	
      this.sendGreetingPageInfoToReviewPage();
      return true;
    }
  },
} 
</script>

