<template>
	<v-container bg fill-height grid-list-md text-xs-center>
		<v-row align="center" justify="center">
			<v-col class="d-flex" cols="10">
				<v-select
					:items="preferredLanguageOptions"
					label="Please select your preferred language"
					required
					:rules="[v => !!v || 'Preferred language field is required']"
					dense
					v-model="preferredLanguage"
				></v-select>
			</v-col>
		</v-row>
		
		<v-row align="center" justify="center">
			<v-col class="d-flex flex-wrap" cols="10">
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
									height="24em"
									width="27em"
									@click="singleRegistration"
							>
								<div>
									<v-icon size="12em">mdi-account</v-icon>
									<span><br /><p style="font-size:1.5em" class="font-weight-regular"><br />Register myself</p></span>
								</div>
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
									height="24em"
									width="27em"
									@click="householdRegistration"
								>
									<div>
									<v-icon size="12em">mdi-account-group</v-icon>
									<span><br /><p style="font-size:1.5em" class="font-weight-regular"><br />Register my household</p></span>
								</div>
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

