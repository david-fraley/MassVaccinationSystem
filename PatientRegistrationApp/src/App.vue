<template>
  <v-app id="Patient-registration"> 
    <v-main>
		<v-container>
			<v-card class="elevation-12" min-width="400" max-width="1000"> 
				<v-stepper v-model="page" class="elevation-0">
					<v-toolbar flat color="primary" dark>
						<!-- We could make the following toolbar dynamic, but for now I just have one title (defined at the end of this file)-->
						<v-spacer /><v-toolbar-title>{{title}}</v-toolbar-title> <v-spacer />
					</v-toolbar>
					<!-- v-stepper-header is the progress bar along the top of the page -->
					<v-stepper-header class="elevation-0">
						<template v-for="n in getNumberOfSteps()">
							<v-stepper-step
								:key="`${n}-step`"
								:complete="page > n"
								color="accent"
								:step="n" 
							>
							</v-stepper-step>

							<v-divider
								v-if="n !== getNumberOfSteps()"
								:key="n"
							></v-divider>
						</template>
					</v-stepper-header>
		
					
					<!--The v-stepper-items holds all of the "page" content we will swap in an out based on the navigation-->
					<v-stepper-items>
						<!-- Greeting Page -->
						<v-stepper-content step="1">
							<v-card flat>			
								<!--listen for changes in the button selections-->				
								<GreetingPage @singleRegistration="setSinglePatientRegistration()" @householdRegistration="setHouseholdRegistration()"
								ref="greetingpage"/>
							</v-card>
							<v-card-actions>								
								<v-spacer></v-spacer>
								<!--Logic to check for single patient registration path -->
								<template v-if="isSinglePatientRegistration()">
									<v-icon large color="secondary"	@click="advanceToSinglePatientRegistration()">
										mdi-chevron-right
									</v-icon>
								</template>
								<!--Logic to check for household registration path -->
								<!-- Just stay on this page for now - until we build the household registration pages -->
								<template v-if="isHouseholdRegistration()">
									<v-icon large color="secondary" @click="advanceToHouseholdRegistration()">
										mdi-chevron-right
									</v-icon>
								</template>
							</v-card-actions>
						</v-stepper-content>

						<!--Logic to check for the "single patient" registration path-->
						<template v-if="isSinglePatientRegistration()">
							<!-- Single Patient: Home Address -->
							<v-stepper-content step="2">
								<v-toolbar flat >
									<v-toolbar-title>Enter Your Home Address</v-toolbar-title>
								</v-toolbar>
								<v-card flat><SinglePatientHomeAddress ref="singlepatienthomeaddress"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifySinglePatientHomeAddress()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>
							
							<!-- Single Patient: Contact Info -->
							<v-stepper-content step="3">
								<v-toolbar flat>
									<v-toolbar-title> Enter Your Contact Information</v-toolbar-title>
								</v-toolbar>
								<v-card flat><SinglePatientContactInfo ref="singlepatientcontactinfo"/></v-card>
								<v-card-actions>
									<v-icon large color="secondary"	@click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifySinglePatientContactInfo()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>
							
							<!-- Single Patient: Personal Info -->
							<v-stepper-content step="4">
								<v-toolbar flat>
									<v-toolbar-title>Enter Your Personal Information</v-toolbar-title>
								</v-toolbar>
								<v-card flat><SinglePatientPersonalInfo ref="singlepatientpersonalinfo"/></v-card>							
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifySinglePatientPersonalInfo()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>
							
							<!-- Single Patient: Emergency Contact -->
							<v-stepper-content step="5">
								<v-toolbar flat>
									<v-toolbar-title>Specify an Emergency Contact</v-toolbar-title>
								</v-toolbar>
								<v-card flat><SinglePatientEmergencyContact ref="singlepatientemergencycontact"/></v-card>
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifySinglePatientEmergencyContact()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>
							
							<!-- Single Patient: Review and Submit -->
							<v-stepper-content step="6">
								<v-toolbar flat>
									<v-toolbar-title>Review and Submit Registration</v-toolbar-title>
								</v-toolbar>
								<v-card flat><SinglePatientReviewSubmit/></v-card>
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-btn large color="secondary" @click="submit()">
										Submit
									</v-btn>
								</v-card-actions>
							</v-stepper-content>
						</template>

						<!--Logic to check for the "household" registration path-->
						<template v-if="isHouseholdRegistration()">

							<!-- Household: Register Number of People -->
							<v-stepper-content step="2">
								<v-toolbar flat>
									<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Register Your Household</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdRegisterNumber/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdRegisterNumber()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<!-- Household: Address -->
							<v-stepper-content step="3">
								<v-toolbar flat>
									<v-toolbar-title>Enter Your Household Address</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdHomeAddress ref="householdhomeaddress"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdHomeAddress()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<!-- Household: Contact Info -->
							<v-stepper-content step="4">
								<v-toolbar flat>
									<v-toolbar-title>Enter Your Household Contact Information</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdContactInfo ref="householdcontactinfo"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdContactInfo()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<!-- Household: Personal Info -->
							<v-stepper-content step="5">
								<v-toolbar flat>
									<v-toolbar-title>Enter Your Personal Information</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdPersonalInfo_1 ref="householdPersonalInfo_1"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdPersonalInfo()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<!-- Household: Emergency Contact -->
							<v-stepper-content step="6">
								<v-toolbar flat>
									<v-toolbar-title>Specify Your Emergency Contact</v-toolbar-title></v-toolbar>
								<v-toolbar flat>
									<v-spacer /><v-subheader>Note: You will be specified as the emergency contact for the rest of your household.</v-subheader><v-spacer />
								</v-toolbar>
								<v-card flat><HouseholdEmergencyContact ref="householdemergencycontact"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdEmergencyContact()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<v-stepper-content step="7">
								<v-toolbar flat>
									<v-toolbar-title>Enter personal information for household member #2</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_n"/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-icon large color="secondary" @click="verifyHouseholdPersonalInfo_n()">
										mdi-chevron-right
									</v-icon>
								</v-card-actions>
							</v-stepper-content>

							<!-- Household: Review and submit -->
							<v-stepper-content step="8">
								<v-toolbar flat>
									<v-toolbar-title>Review and submit registration</v-toolbar-title>
								</v-toolbar>
								<v-card flat><HouseholdReviewSubmit/></v-card>				
								<v-card-actions>
									<v-icon large color="secondary" @click="goToPreviousPage()">
										mdi-chevron-left
									</v-icon>
									<v-spacer></v-spacer>
									<v-btn large color="secondary" @click="submit()">
										Submit
									</v-btn>
								</v-card-actions>
							</v-stepper-content>
						</template>
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
import HouseholdRegisterNumber from './components/HouseholdRegisterNumber';
import HouseholdHomeAddress from './components/HouseholdHomeAddress';
import HouseholdContactInfo from './components/HouseholdContactInfo';
import HouseholdPersonalInfo_1 from './components/HouseholdPersonalInfo_1';
import HouseholdEmergencyContact from './components/HouseholdEmergencyContact';
import HouseholdPersonalInfo_n from './components/HouseholdPersonalInfo_n';
import HouseholdReviewSubmit from './components/HouseholdReviewSubmit';
import config from './config.js';

export default {
	name: 'App',
	methods: 
	{
		submit() {
			alert('You clicked submit!');
		},
		goToPage(pageNum) {
			this.page=pageNum
		},
		verifyGreetingPage() {
			this.$refs.greetingpage.verifyFormContents() ? 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE) : 
			this.goToPage(config.registrationPages.GREETING_PAGE);
		},
		verifySinglePatientHomeAddress() {
			this.$refs.singlepatienthomeaddress.verifyFormContents() ? 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE) : 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE);
		},
		verifySinglePatientContactInfo() {
			this.$refs.singlepatientcontactinfo.verifyFormContents() ? 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE) : 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE);
		},
		verifySinglePatientPersonalInfo() {
			this.$refs.singlepatientpersonalinfo.verifyFormContents() ? 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE) : 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE);
		},
		verifySinglePatientEmergencyContact() {
			this.$refs.singlepatientemergencycontact.verifyFormContents() ? 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE) : 
			this.goToPage(config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE);
		},	
		verifyHouseholdRegisterNumber() {
			this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE);
		},
		verifyHouseholdHomeAddress() {
			this.$refs.householdhomeaddress.verifyFormContents() ?
			this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE) :
			this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE);
		},
		verifyHouseholdContactInfo() {
			this.$refs.householdcontactinfo.verifyFormContents() ?
			this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_1_PAGE) :
			this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE);
		},
		verifyHouseholdPersonalInfo() {
			this.$refs.householdPersonalInfo_1.verifyFormContents() ?
			this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE):
			this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_1_PAGE);
		},
		verifyHouseholdEmergencyContact() {
			this.$refs.householdemergencycontact.verifyFormContents() ?
			this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_N_PAGE):
			this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE);
		},
		verifyHouseholdPersonalInfo_n() {
			this.$refs.householdPersonalInfo_n.verifyFormContents() ?
			this.goToPage(config.registrationPages.HOUSEHOLD_REVIEW_SUBMIT_PAGE):
			this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_N_PAGE);
		},
		setSinglePatientRegistration() {
			this.registrationPath = config.selectedRegistrationPath.SINGLE_PATIENT_REGISTRATION_PATH;
		},
		setHouseholdRegistration() {
			this.registrationPath = config.selectedRegistrationPath.HOUSEHOLD_REGISTRATION_PATH;
		},
		isSinglePatientRegistration() {
			let returnValue = true;
			(this.registrationPath == config.selectedRegistrationPath.SINGLE_PATIENT_REGISTRATION_PATH) ? returnValue = true: returnValue = false;
			return returnValue;
		},
		isHouseholdRegistration() {
			let returnValue = true;
			(this.registrationPath == config.selectedRegistrationPath.HOUSEHOLD_REGISTRATION_PATH) ? returnValue = true: returnValue = false;
			return returnValue;
		},
		isGreetingPage() {
			let returnValue = true;
			(this.page == config.registrationPages.GREETING_PAGE) ? returnValue = true: returnValue = false;
			return returnValue;
		},
		goToPreviousPage() {
			let currentPage = this.page;
			(currentPage > config.registrationPages.GREETING_PAGE) ? currentPage-- : currentPage;
			
			this.goToPage(currentPage)
		},
		advanceToSinglePatientRegistration() {
			if(this.$refs.greetingpage.verifyFormContents())
			{
				this.goToPage(config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE)
			}
		},
		advanceToHouseholdRegistration() {
			if(this.$refs.greetingpage.verifyFormContents())
			{
				this.goToPage(config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE)
			}
		},
		getNumberOfSteps() {
			let numberOfSteps = 0;
			this.isSinglePatientRegistration() ? numberOfSteps = config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE : numberOfSteps = config.registrationPages.HOUSEHOLD_REVIEW_SUBMIT_PAGE;
			return numberOfSteps;
		}
	},
	mounted() 
	{
		EventBus.$on('DATA_HOUSEHOLD_COUNT_UPDATED', (householdCountPayload) => {
			this.setNumberOfHouseholdMembers(householdCountPayload)
		})
	},
	components: 
	{
		GreetingPage,
		SinglePatientPersonalInfo,
		SinglePatientHomeAddress,
		SinglePatientContactInfo,
		SinglePatientEmergencyContact,
		SinglePatientReviewSubmit,
		HouseholdRegisterNumber,
		HouseholdHomeAddress,
		HouseholdContactInfo,
		HouseholdPersonalInfo_1,
		HouseholdEmergencyContact,
		HouseholdPersonalInfo_n,
		HouseholdReviewSubmit
	},
	data () {
		return {
			page: config.registrationPages.GREETING_PAGE,
			title: 'COVID-19 Vaccination Registration',
			registrationPath: config.selectedRegistrationPath.NO_PATH_SELECTED,
			}
		},
  }

</script>
