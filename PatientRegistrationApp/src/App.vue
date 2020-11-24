<template>
  <v-app id="Patient-registration">  
    <v-main>
		<v-container fill-height>
			<v-layout justify-center align-center>
				<v-card flat height="100%" width="100%" class="d-flex flex-column"> 
					<v-stepper v-model="page" class="elevation-0">	
						<v-toolbar flat height="100" color="primary" dark>
							<v-toolbar-title style="font-size:3em" class="font-weight-bold">{{title}}</v-toolbar-title>
							<v-spacer></v-spacer>
							<v-img
								max-height="100"
								max-width="130"
								src="./assets/Logo.png"
							></v-img>
						</v-toolbar>
						<v-stepper-header class="elevation-0">
							<template v-if="isSinglePatientRegistration() || isHouseholdRegistration()">
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
							</template>
						</v-stepper-header>
						
						<!--The v-stepper-items holds all of the "page" content we will swap in an out based on the navigation-->
						<v-stepper-items>
							<!-- Greeting Page -->
							<v-stepper-content step="1">
								<v-card flat>							
									<GreetingPage @singleRegistration="setSinglePatientRegistration()" @householdRegistration="setHouseholdRegistration()"
									ref="greetingpage"/>
								</v-card>
							</v-stepper-content>

							<!--Logic to check for the "single patient" registration path-->
							<template v-if="isSinglePatientRegistration()">
								<!-- Single Patient: Home Address -->
								<v-stepper-content step="2">
									<v-toolbar flat >
										<v-toolbar-title>Enter your home address</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientHomeAddress ref="singlepatienthomeaddress"/></v-card>				
								</v-stepper-content>
								
								<!-- Single Patient: Contact Info -->
								<v-stepper-content step="3">
									<v-toolbar flat>
										<v-toolbar-title>Enter your contact information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientContactInfo ref="singlepatientcontactinfo"/></v-card>
								</v-stepper-content>
								
								<!-- Single Patient: Personal Info -->
								<v-stepper-content step="4">
									<v-toolbar flat>
										<v-toolbar-title>Enter your personal information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientPersonalInfo ref="singlepatientpersonalinfo"/></v-card>							
								</v-stepper-content>
								
								<!-- Single Patient: Emergency Contact -->
								<v-stepper-content step="5">
									<v-toolbar flat>
										<v-toolbar-title> Specify an emergency contact</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientEmergencyContact ref="singlepatientemergencycontact"/></v-card>
								</v-stepper-content>
								
								<!-- Single Patient: Review and Submit -->
								<v-stepper-content step="6">
									<v-toolbar flat>
										<v-toolbar-title>Please ensure your information is correct</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientReviewSubmit/></v-card>
								</v-stepper-content>
							</template>

							<!--Logic to check for the "household" registration path-->
							<template v-if="isHouseholdRegistration()">

								<!-- Household: Register Number of People -->
								<v-stepper-content step="2">
									<v-toolbar flat>
										<v-toolbar-title>Register your household</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdRegisterNumber ref="householdregisternumber"/></v-card>				
								</v-stepper-content>

								<!-- Household: Address -->
								<v-stepper-content step="3">
									<v-toolbar flat>
										<v-toolbar-title>Enter your household address</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdHomeAddress ref="householdhomeaddress"/></v-card>				
								</v-stepper-content>

								<!-- Household: Contact Info -->
								<v-stepper-content step="4">
									<v-toolbar flat>
										<v-toolbar-title>Enter your household contact information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdContactInfo ref="householdcontactinfo"/></v-card>				
								</v-stepper-content>

								<!-- Household: Personal Info -->
								<v-stepper-content step="5">
									<v-toolbar flat>
										<v-toolbar-title>Enter your personal information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdPersonalInfo_1 ref="householdPersonalInfo_1"/></v-card>				
								</v-stepper-content>

								<!-- Household: Emergency Contact -->
								<v-stepper-content step="6">
									<v-toolbar flat>
										<v-toolbar-title>Specify your emergency contact</v-toolbar-title></v-toolbar>
									<v-toolbar flat>
										<v-subheader>Note: You will be specified as the emergency contact for the rest of your household.</v-subheader>
									</v-toolbar>
									<v-card flat><HouseholdEmergencyContact ref="householdemergencycontact"/></v-card>				
								</v-stepper-content>

								<!-- Household: Personal Info #n -->
								<template v-for="n in getNumberOfHouseholdMembers()-1">
									<v-stepper-content
									:key="`${n+1}-member`"
									:step="n+6">
										<v-toolbar flat>
											<v-toolbar-title>Enter personal information for household member #{{n+1}}</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo" v-bind:householdMemberNumber="n+1"></HouseholdPersonalInfo_n></v-card>
									</v-stepper-content>
								</template>

								<!-- Household: Review and submit -->
								<v-stepper-content :step="getNumberOfSteps()">
									<v-toolbar flat>
										<v-toolbar-title>Review and submit registration</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>
								</v-stepper-content>

							</template>
						</v-stepper-items>
					</v-stepper>
					
					<!--navigation footer along the bottom of the page -->
					<v-footer absolute color="white">
						<template v-if="isGreetingPage()">
							<v-spacer></v-spacer>
							<v-icon large color="secondary" @click="goToNextPage()">
								mdi-chevron-right
							</v-icon>
						</template>
						<template v-else-if="isSinglePatientReviewSubmit() && isSinglePatientRegistration()">
							<v-icon large color="secondary" @click="goToPreviousPage()">
								mdi-chevron-left
							</v-icon>
							<v-spacer></v-spacer>
							<v-btn large color="secondary" @click="submit()">
								Submit
							</v-btn>
						</template>
						<template v-else-if="isHouseholdPatientReviewSubmit()  && isHouseholdRegistration()">
							<v-icon large color="secondary" @click="goToPreviousPage()">
								mdi-chevron-left
							</v-icon>
							<v-spacer></v-spacer>
							<v-btn large color="secondary" @click="submit()">
								Submit
							</v-btn>
						</template>
						<template v-else>
							<v-icon large color="secondary" @click="goToPreviousPage()">
								mdi-chevron-left
							</v-icon>
							<v-spacer></v-spacer>
							<v-icon large color="secondary" @click="goToNextPage()">
								mdi-chevron-right
							</v-icon>
						</template>
					</v-footer>
				</v-card>
			</v-layout>
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
import EventBus from './eventBus'
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
		setSinglePatientRegistration() {
			this.registrationPath = config.selectedRegistrationPath.SINGLE_PATIENT_REGISTRATION_PATH;
		},
		setHouseholdRegistration() {
			this.registrationPath = config.selectedRegistrationPath.HOUSEHOLD_REGISTRATION_PATH;
		},
		setNumberOfHouseholdMembers(householdCountPayload)
		{
			if(this.numberOfHouseholdMembers != householdCountPayload.householdCount)
			{
				this.numberOfHouseholdMembers = householdCountPayload.householdCount;
			}
		},
		getNumberOfHouseholdMembers()
		{
			return this.numberOfHouseholdMembers;
		},
		setHouseholdFamilyName(householdFamilyName)
		{
			this.householdFamilyName = householdFamilyName;
			
			for(var i=0;i<this.getNumberOfHouseholdMembers();i++)
			{
				this.$refs.householdPersonalInfo[i].setHouseholdFamilyName(this.householdFamilyName)
			}
		},
		getHouseholdFamilyName()
		{
			return this.householdFamilyName;
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
		isSinglePatientReviewSubmit() {
			let returnValue = true;
			(this.page == config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE) ? returnValue = true: returnValue = false;
			return returnValue;
		},
		isHouseholdPatientReviewSubmit() {
			let returnValue = true;
			(this.page == this.numberOfHouseholdMembers+config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE) ? returnValue = true: returnValue = false;
			return returnValue;
		},
		goToPreviousPage() {
			let currentPage = this.page;
			(currentPage > config.registrationPages.GREETING_PAGE) ? currentPage-- : currentPage;
			
			this.goToPage(currentPage)
		},
		goToNextPage()
		{
			if(this.isSinglePatientRegistration())
			{
				switch(this.page)
				{
					case config.registrationPages.GREETING_PAGE:
						if(this.$refs.greetingpage.verifyFormContents())
						{
							this.goToPage(config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE)
						}
						break;
					case config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE:
						this.$refs.singlepatienthomeaddress.verifyFormContents() ? 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE) : 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_HOME_ADDRESS_PAGE);
						break;
					case config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE:
						this.$refs.singlepatientcontactinfo.verifyFormContents() ? 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE) : 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_CONTACT_INFO_PAGE);
						break;
					case config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE:
						this.$refs.singlepatientpersonalinfo.verifyFormContents() ? 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE) : 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_PERSONAL_INFO_PAGE);
						break;
					case config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE:
						this.$refs.singlepatientemergencycontact.verifyFormContents() ? 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE) : 
						this.goToPage(config.registrationPages.SINGLE_PATIENT_EMERGENCY_CONTACT_PAGE);
						break;
					default:
						alert(this.page)
						break;
				}									
			}
			else if(this.isHouseholdRegistration())
			{
				switch(true)
				{
					case (this.page == config.registrationPages.GREETING_PAGE):
						if(this.$refs.greetingpage.verifyFormContents())
						{
							this.goToPage(config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE)
						}
						break;
					case (this.page == config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE):
						this.$refs.householdregisternumber.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE) :
						this.goToPage(config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE);
						break;
					case (this.page == config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE):
						this.$refs.householdhomeaddress.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE);
						break;
					case (this.page == config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE):
						this.$refs.householdcontactinfo.verifyFormContents()?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE);
						break;
					case (this.page == config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE):
						this.$refs.householdPersonalInfo_1.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE);
						break;
					case (this.page == config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE):
						this.$refs.householdemergencycontact.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE);
						break;
					case (this.page - config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE <= 18):
						if (this.$refs.householdPersonalInfo[this.page - config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE].verifyFormContents()) {
							this.page++;
						}
						break;
					default:
						alert(this.page)
						break;
				}
			}
		},
		getNumberOfSteps() {
			let numberOfSteps = 0;
			if(this.isSinglePatientRegistration())
			{
				numberOfSteps = config.registrationPages.SINGLE_PATIENT_REVIEW_SUBMIT_PAGE;
			}
			else if(this.isHouseholdRegistration())
			{
				numberOfSteps = this.numberOfHouseholdMembers+config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE;
			}
				
			return numberOfSteps;
		}
	},
	mounted() 
	{
		EventBus.$on('DATA_HOUSEHOLD_COUNT_UPDATED', (householdCountPayload) => {
			this.setNumberOfHouseholdMembers(householdCountPayload)
		}),
		EventBus.$on('DATA_HOUSEHOLD_FAMILY_NAME', (householdFamilyName) => {
			this.setHouseholdFamilyName(householdFamilyName)
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
			numberOfHouseholdMembers: 2,
			householdFamilyName: ''
			}
		},
  }
</script>
<style lang="css" scoped>

	.v-stepper__step {
		padding: 16px;
	}

	.v-toolbar__title {
		font-size:1.5rem
	} 

</style>