<template>

  <v-app id="Patient-registration"> 
    <v-main>
		
		<v-container>
			<v-card class="elevation-12" min-width="400" max-width="1000"> 
				<v-stepper v-model="page">	
					<!-- Logic to check if the page is the greeting page; if so, don't show the stepper-header because we don't know how many steps are involved until a registration path is chosen-->
					<template v-if="page != '1'">
						<!--Logic to check for the "single patient" registration path-->
						<template v-if="registrationPath == '1'"> <!--TO DO:  can we set a #DEFINE or equivalent?? -->
							<!--the v-stepper-header is the progress bar along the top of the container-->
							<v-stepper-header>
								<!-- Greeting Page -->
								<v-stepper-step color="accent"
									:complete="page > 1"
									step="1"
								></v-stepper-step>
								
								<v-divider></v-divider>
								
								<!-- Home Address -->
								<v-stepper-step color="accent"
									:complete="page > 2"
									step="2"
								></v-stepper-step>

								<v-divider></v-divider>
								
								<!-- Contact Info -->
								<v-stepper-step color="accent"
									:complete="page > 3"
									step="3"
								></v-stepper-step>

								<v-divider></v-divider>
								
								<!-- Personal Info -->
								<v-stepper-step color="accent"
									:complete="page > 4"
									step="4"
								></v-stepper-step>

								<v-divider></v-divider>
								
								<!-- Emergency Contact -->
								<v-stepper-step color="accent"
									:complete="page > 5"
									step="5"
								></v-stepper-step>

								<v-divider></v-divider>

								<!-- Review and Submit -->
								<v-stepper-step color="accent"
									step="6"
								></v-stepper-step>
							</v-stepper-header>
						</template>
					</template>
					
					<v-toolbar color="primary" dark>
						<!-- We could make the following toolbar dynamic, but for now I just have one title (defined at the end of this file)-->
						<v-toolbar-title>{{title}}</v-toolbar-title>
					</v-toolbar>			
					
					<!--The v-stepper-items holds all of the "page" content we will swap in an out based on the navigation-->
					<v-stepper-items>
						<!-- Greeting Page -->
						<v-stepper-content step="1">
							<v-card flat>			
								<!--listen for changes in the button selections-->				
								<GreetingPage @singleRegistration="setSinglePatientRegistration" @householdRegistration="setHouseholdRegistration"/>
							</v-card>
							<v-card-actions>								
								<v-spacer></v-spacer>
								<!--Logic to check for single patient registration path -->
								<template v-if="registrationPath == '1'">
									<v-icon large color="secondary"
										@click="page=2"
									>
										mdi-chevron-right
									</v-icon>
								</template>
								<!--Logic to check for household registration path -->
								<!-- Just stay on this page for now - until we build the household registration pages -->
								<template v-if="registrationPath == '2'">
									<v-icon large color="secondary"
										@click="page=1" 
									>
										mdi-chevron-right
									</v-icon>
								</template>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Home Address -->
						<v-stepper-content step="2">
							<v-toolbar flat >
								<v-toolbar-title>Enter your home address</v-toolbar-title>
							</v-toolbar>
							<v-card flat>
								<SinglePatientHomeAddress ref="singlepatienthomeaddress"/>
							</v-card>				
							<v-card-actions>
								<v-icon large color="secondary"
									@click="goToPage(1)"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon large color="secondary"
									@click="verifySinglePatientHomeAddress"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						<!-- Contact Info -->
						<v-stepper-content step="3">
							<v-toolbar flat>
								<v-toolbar-title>Enter your contact information</v-toolbar-title>
							</v-toolbar>
							<v-card flat>
								<SinglePatientContactInfo ref="singlepatientcontactinfo"/>
							</v-card>
							<v-card-actions>
								<v-icon large color="secondary"
									@click="goToPage(2)"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon large color="secondary"
									@click="verifySinglePatientContactInfo"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Personal Info -->
						<v-stepper-content step="4">
							<v-toolbar flat>
								<v-toolbar-title>Enter your personal information</v-toolbar-title>
							</v-toolbar>
							<v-card flat>
								<SinglePatientPersonalInfo ref="singlepatientpersonalinfo"/>
							</v-card>							
							<v-card-actions>
								<v-icon large color="secondary"
									@click="goToPage(3)"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon large color="secondary"
									@click="verifySinglePatientPersonalInfo"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Emergency Contact -->
						<v-stepper-content step="5">
							<v-toolbar flat>
								<v-toolbar-title>Specify an emergency contact</v-toolbar-title>
							</v-toolbar>
							<v-card flat>
								<SinglePatientEmergencyContact ref="singlepatientemergencycontact"/>
							</v-card>
							<v-card-actions>
								<v-icon large color="secondary"
									@click="goToPage(4)"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-icon large color="secondary"
									@click="verifySinglePatientEmergencyContact"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Review and Submit -->
						<v-stepper-content step="6">
							<v-toolbar flat>
								<v-toolbar-title>Review and submit registration</v-toolbar-title>
							</v-toolbar>
							<v-card flat>
								<SinglePatientReviewSubmit/>
							</v-card>
							<v-card-actions>
								<v-icon large color="secondary"
									@click="goToPage(5)"
								>
									mdi-chevron-left
								</v-icon>
								<v-spacer></v-spacer>
								<v-btn large color="secondary"
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
		submit()
		{
			alert('You clicked submit!')
		},
		goToPage(pageNum)
		{
			this.page=pageNum
		},
		verifySinglePatientHomeAddress()
		{
			this.$refs.singlepatienthomeaddress.verifyFormContents() ? this.goToPage(3) : this.goToPage(2);
		},
		verifySinglePatientContactInfo()
		{
			this.$refs.singlepatientcontactinfo.verifyFormContents() ? this.goToPage(4) : this.goToPage(3);
		},
		verifySinglePatientPersonalInfo()
		{
			this.$refs.singlepatientpersonalinfo.verifyFormContents() ? this.goToPage(5) : this.goToPage(4);
		},
		verifySinglePatientEmergencyContact()
		{
			this.$refs.singlepatientemergencycontact.verifyFormContents() ? this.goToPage(6) : this.goToPage(5);
		},	
		setSinglePatientRegistration()
		{
			this.registrationPath = 1;
		},
		setHouseholdRegistration()
		{
			this.registrationPath = 2;
		}, 
	},
	components: 
	{
		GreetingPage,
		SinglePatientHomeAddress,
		SinglePatientContactInfo,
		SinglePatientPersonalInfo,
		SinglePatientEmergencyContact,
		SinglePatientReviewSubmit,
	},
	data () {
		return {
			page: 1,
			title: 'COVID-19 Vaccination Registration',
			registrationPath: 0
		}
	}
}
</script>
