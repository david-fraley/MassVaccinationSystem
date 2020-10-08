<template>

  <v-app id="Patient-registration">
  
    <v-main>
		<v-container>
			
			<v-card 
				class="elevation-12"
				min-width="400"
				max-width="1000"
			>
				<!--the v-stepper is the progress bar along the top of the container-->
				<v-stepper 
					v-model="page"
				>	
					<v-stepper-header>
						<!-- Greeting Page -->
						<v-stepper-step
							:complete="page > 1"
							step="1"
							color="accent"
						></v-stepper-step>
						
						<v-divider></v-divider>
						
						<!-- Home Address -->
						<v-stepper-step
							:complete="page > 2"
							step="2"
							color="accent"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Contact Info -->
						<v-stepper-step
							:complete="page > 3"
							step="3"
							color="accent"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Personal Info -->
						<v-stepper-step
							:complete="page > 4"
							step="4"
							color="accent"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Emergency Contact -->
						<v-stepper-step
							:complete="page > 5"
							step="5"
							color="accent"
						></v-stepper-step>

						<v-divider></v-divider>

						<!-- Review and Submit -->
						<v-stepper-step 
							step="6"
							color="accent"
						></v-stepper-step>
					</v-stepper-header>
					
					<v-toolbar
						color="primary"
						dark
					>
						<!-- We could make the following toolbar dynamic, but for now I just have one title (defined at the end of this file)-->
						<v-toolbar-title>{{title}}</v-toolbar-title>
					</v-toolbar>			
					
					<!--The v-stepper-items holds all of the "page" content we will swap in an out based on the navigation-->
					<v-stepper-items>
						
						<!-- Greeting Page -->
						<v-stepper-content step="1">
							<v-card
								flat
							>							
								<GreetingPage/>
							</v-card>
							<v-card-actions>								
								<v-spacer></v-spacer>
								<v-icon
									large
									color="secondary"
									@click="page=2"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Home Address -->
						<v-stepper-content step="2">
							<v-toolbar
								flat
							>
								<v-toolbar-title>Enter your home address</v-toolbar-title>
							</v-toolbar>
							<v-card
								flat
							>
								<SinglePatientHomeAddress/>
							</v-card>				
							<v-card-actions>
								<v-icon
									large
									color="secondary"
									@click="page=1"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon
									large
									color="secondary"
									@click="y = confirmForm(); if (y == 1) page=3;"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						<!-- Contact Info -->
						<v-stepper-content step="3">
							<v-toolbar
								flat
							>
								<v-toolbar-title>Enter your contact information</v-toolbar-title>
							</v-toolbar>
							<v-card
								flat
							>
								<SinglePatientContactInfo/>
							</v-card>
							<v-card-actions>
								<v-icon
									large
									color="secondary"
									@click="page=2"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon
									large
									color="secondary"
									@click="page=4"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Personal Info -->
						<v-stepper-content step="4">
							<v-toolbar
								flat
							>
								<v-toolbar-title>Enter your personal information</v-toolbar-title>
							</v-toolbar>
							<v-card
								flat
							>
								<SinglePatientPersonalInfo/>
							</v-card>							
							<v-card-actions>
								<v-icon
									large
									color="secondary"
									@click="page=3"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon
									large
									color="secondary"
									@click="page=5"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Emergency Contact -->
						<v-stepper-content step="5">
							<v-toolbar
								flat
							>
								<v-toolbar-title>Specify an emergency contact</v-toolbar-title>
							</v-toolbar>
							<v-card
								flat
							>
								<SinglePatientEmergencyContact/>
							</v-card>
							<v-card-actions>
								<v-icon
									large
									color="secondary"
									@click="page=4"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon
									large
									color="secondary"
									@click="page=6"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Review and Submit -->
						<v-stepper-content step="6">
							<v-toolbar
								flat
							>
								<v-toolbar-title>Review and submit registration</v-toolbar-title>
							</v-toolbar>
							<v-card
								flat
							>
								<SinglePatientReviewSubmit/>
							</v-card>
							<v-card-actions>
								<v-icon
									large
									color="secondary"
									@click="page=5"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-btn
									large
									color="secondary"
									@click="submit"
								>
									Submit
								</v-btn>
							</v-card-actions>
						</v-stepper-content>

					</v-stepper-items>
				</v-stepper>
			</v-card>
		</v-container>
	</v-main>
  </v-app>
  
</template>

<script>
import GreetingPage from './components/GreetingPage';
import SinglePatientHomeAddress  from './components/SinglePatientHomeAddress';
import SinglePatientContactInfo from './components/SinglePatientContactInfo';
import SinglePatientPersonalInfo from './components/SinglePatientPersonalInfo';
import SinglePatientEmergencyContact from './components/SinglePatientEmergencyContact';
import SinglePatientReviewSubmit from './components/SinglePatientReviewSubmit';


export default {
	name: 'App',
	methods: {
	
		confirmForm() {
			var valid = true
			var message = "Woops! You need to enter the following fields:"
			
			var a = document.getElementById("addr")
			a.required = true
			if (a.value == "") {
				message += " *address"
				valid = false
			}
			
		var b = document.getElementById("city")
			b.required = true
			if (b.value == "") {
				message += " *city"
				valid = false
			}
			
		var c = document.getElementById("state")
			c.required = true
			if (c.value == "") {
				message += " *state"
				valid = false
			}
			
		var d = document.getElementById("county")
			d.required = true
			if (d.value == "") {
				message += " *county"
				valid = false
			}
			
		var e = document.getElementById("country")
			e.required = true
			if (e.value == "") {
				message += " *country"
				valid = false
			}
			
		var f = document.getElementById("zipcode")
			f.required = true
			if (f.value == "") {
				message += " *zipcode"
				valid = false
			}

		if (valid == false) {
			alert(message)
			return -1
		}
		return 1
},
		submit()
		{
			alert('You clicked submit!')
		}	
	},
  components: 
  {
	GreetingPage,
	SinglePatientPersonalInfo,
	SinglePatientHomeAddress,
	SinglePatientContactInfo,
	SinglePatientEmergencyContact,
	SinglePatientReviewSubmit,
  },

  data () {
	return {
		page: 1,
		title: 'COVID-19 Vaccination Registration',
		}
	},
  
}
</script>
