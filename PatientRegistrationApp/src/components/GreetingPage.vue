<template>
	<v-container bg fill-height grid-list-md text-xs-center>
		<v-row>
			
			<v-divider></v-divider>
			
		</v-row>
		<v-row align="left" justify="left">
			<span><p style="font-size:1.5em" class="font-weight-regular"><br />Select your preferred language</p></span>
		</v-row>
		<v-row align="center" justify="center">
			<v-col class="d-flex" cols="4">
				
				<v-select
					:items="preferredLanguageOptions"
					label=" "
					required
					:rules="[v => !!v || 'Preferred language field is required']"
					v-model="preferredLanguage"
				></v-select>
			</v-col>
		</v-row>
		<v-row>
			
			<v-divider></v-divider>
			
		</v-row>
		<v-row align="left" justify="left">
			<span><p style="font-size:1.5em" class="font-weight-regular"><br />Select your registration type</p></span>
		</v-row>
		
		<v-row align="center" justify="center">
			
				<v-btn-toggle
					v-model="text"
					group
				>
							<v-btn 
									class="ma-2"
									outlined 
									x-large 
									color="primary"
									v-bind="attrs"
									v-on="on"
									height="18em"
									width="22em"
									@click="singleRegistration"
							>
								<div>
									<v-icon size="9em">mdi-account</v-icon>
									<span><br /><p style="font-size:1.5em" class="font-weight-regular"><br />Register myself</p></span>
								</div>
							</v-btn>

					<v-tooltip bottom max-width="20%">
						<template v-slot:activator="{on, attrs}">
							<v-btn 
									class="ma-2"
									outlined 
									x-large 
									color="primary"
									v-bind="attrs"
									v-on="on"
									height="18em"
									width="22em"
									@click="householdRegistration"
								>
									<div>
									<v-icon size="9em">mdi-account-group</v-icon>
									<span><br /><p style="font-size:1.5em" class="font-weight-regular"><br />Register my household</p></span>
								</div>
							</v-btn>
						</template>
						<span>A household is composed of any people who occupy the same given housing unit. If you are planning to register your household, all members must currently be residing in the same address. If members of a household are residing in other housing units, it is necessary to register any given members individually. Max household registration of 20 registrants.</span>
					</v-tooltip>
				</v-btn-toggle>
			
		</v-row>
	</v-container>
</template>
<script>
import EventBus from '../eventBus'

export default {
	name: "GreetingPage",
	data: () => ({
		preferredLanguageOptions: ['English','Spanish'],
		preferredLanguage: ''
	
	}),
	methods: 
	{
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
			var valid = true
			var message = "Woops! You need to enter the following field:"
		
				if (this.preferredLanguage == "") {
					message += " Preferred Language"
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
<style lang="css" scoped>


.v-select >>> .v-select__selections {
    font-size: 1em;
	line-height: 1.1em;
}
.v-select >>> .v-select__slot {
    font-size: 1.5em;
}
.v-select >>> .v-list-item__title {
    font-size: 1.5em;
}

</style>

