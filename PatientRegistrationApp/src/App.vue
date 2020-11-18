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
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your home address</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientHomeAddress ref="singlepatienthomeaddress"/></v-card>				
								</v-stepper-content>
								
								<!-- Single Patient: Contact Info -->
								<v-stepper-content step="3">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your contact information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientContactInfo ref="singlepatientcontactinfo"/></v-card>
								</v-stepper-content>
								
								<!-- Single Patient: Personal Info -->
								<v-stepper-content step="4">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your personal information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientPersonalInfo ref="singlepatientpersonalinfo"/></v-card>							
								</v-stepper-content>
								
								<!-- Single Patient: Emergency Contact -->
								<v-stepper-content step="5">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular"> Specify an emergency contact</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientEmergencyContact ref="singlepatientemergencycontact"/></v-card>
								</v-stepper-content>
								
								<!-- Single Patient: Review and Submit -->
								<v-stepper-content step="6">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Please ensure your information is correct</v-toolbar-title>
									</v-toolbar>
									<v-card flat><SinglePatientReviewSubmit/></v-card>
								</v-stepper-content>
							</template>

							<!--Logic to check for the "household" registration path-->
							<template v-if="isHouseholdRegistration()">

								<!-- Household: Register Number of People -->
								<v-stepper-content step="2">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Register your household</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdRegisterNumber ref="householdregisternumber"/></v-card>				
								</v-stepper-content>

								<!-- Household: Address -->
								<v-stepper-content step="3">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your household address</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdHomeAddress ref="householdhomeaddress"/></v-card>				
								</v-stepper-content>

								<!-- Household: Contact Info -->
								<v-stepper-content step="4">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your household contact information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdContactInfo ref="householdcontactinfo"/></v-card>				
								</v-stepper-content>

								<!-- Household: Personal Info -->
								<v-stepper-content step="5">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter your personal information</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdPersonalInfo_1 ref="householdPersonalInfo_1"/></v-card>				
								</v-stepper-content>

								<!-- Household: Emergency Contact -->
								<v-stepper-content step="6">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Specify your emergency contact</v-toolbar-title></v-toolbar>
									<v-toolbar flat>
										<v-subheader>Note: You will be specified as the emergency contact for the rest of your household.</v-subheader>
									</v-toolbar>
									<v-card flat><HouseholdEmergencyContact ref="householdemergencycontact"/></v-card>				
								</v-stepper-content>

								<!-- Household: Personal Info, household Member #2 -->
								<v-stepper-content step="7">
									<v-toolbar flat>
										<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #2</v-toolbar-title>
									</v-toolbar>
									<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_2" v-bind:householdMemberNumber="2"></HouseholdPersonalInfo_n></v-card>				
								</v-stepper-content>

								<!-- Household: Personal Info, household Member #3 -->
								<template v-if="getNumberOfHouseholdMembers()>2">
									<v-stepper-content step="8">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #3</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_3" v-bind:householdMemberNumber="3"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="8">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>
								
								<!-- Household: Personal Info, household Member #4 -->
								<template v-if="getNumberOfHouseholdMembers()>3">
									<v-stepper-content step="9">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #4</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_4" v-bind:householdMemberNumber="4"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="9">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #5 -->
								<template v-if="getNumberOfHouseholdMembers()>4">
									<v-stepper-content step="10">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #5</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_5" v-bind:householdMemberNumber="5"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="10">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #6 -->
								<template v-if="getNumberOfHouseholdMembers()>5">
									<v-stepper-content step="11">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #6</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_6" v-bind:householdMemberNumber="6"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="11">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #7 -->
								<template v-if="getNumberOfHouseholdMembers()>6">
									<v-stepper-content step="12">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #7</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_7" v-bind:householdMemberNumber="7"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="12">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #8 -->
								<template v-if="getNumberOfHouseholdMembers()>7">
									<v-stepper-content step="13">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #8</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_8" v-bind:householdMemberNumber="8"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="13">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #9 -->
								<template v-if="getNumberOfHouseholdMembers()>8">
									<v-stepper-content step="14">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #9</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_9" v-bind:householdMemberNumber="9"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="14">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #10 -->
								<template v-if="getNumberOfHouseholdMembers()>9">
									<v-stepper-content step="15">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #10</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_10" v-bind:householdMemberNumber="10"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="15">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #11 -->
								<template v-if="getNumberOfHouseholdMembers()>10">
									<v-stepper-content step="16">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #11</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_11" v-bind:householdMemberNumber="10"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="16">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #12 -->
								<template v-if="getNumberOfHouseholdMembers()>11">
									<v-stepper-content step="17">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #12</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_12" v-bind:householdMemberNumber="12"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="17">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #13 -->
								<template v-if="getNumberOfHouseholdMembers()>12">
									<v-stepper-content step="18">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #13</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_13" v-bind:householdMemberNumber="13"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="18">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #14 -->
								<template v-if="getNumberOfHouseholdMembers()>13">
									<v-stepper-content step="19">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #14</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_14" v-bind:householdMemberNumber="14"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="19">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #15 -->
								<template v-if="getNumberOfHouseholdMembers()>14">
									<v-stepper-content step="20">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #15</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_15" v-bind:householdMemberNumber="15"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="20">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #16 -->
								<template v-if="getNumberOfHouseholdMembers()>15">
									<v-stepper-content step="21">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #16</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_16" v-bind:householdMemberNumber="16"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="21">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #17 -->
								<template v-if="getNumberOfHouseholdMembers()>16">
									<v-stepper-content step="22">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #17</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_17" v-bind:householdMemberNumber="17"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="22">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #18 -->
								<template v-if="getNumberOfHouseholdMembers()>17">
									<v-stepper-content step="23">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #18</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_18" v-bind:householdMemberNumber="18"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="23">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #19 -->
								<template v-if="getNumberOfHouseholdMembers()>18">
									<v-stepper-content step="24">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #19</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_19" v-bind:householdMemberNumber="19"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="24">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

								<!-- Household: Personal Info, household Member #20 -->
								<template v-if="getNumberOfHouseholdMembers()>19">
									<v-stepper-content step="25">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Enter personal information for household member #20</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdPersonalInfo_n ref="householdPersonalInfo_20" v-bind:householdMemberNumber="20"></HouseholdPersonalInfo_n></v-card>				
									</v-stepper-content>
									<!-- Household: Review and submit -->
									<v-stepper-content step="26">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>
								<template v-else>
									<!-- Household: Review and submit -->
									<v-stepper-content step="25">
										<v-toolbar flat>
											<v-toolbar-title style="font-size:1.5em" class="font-weight-regular">Review and submit registration</v-toolbar-title>
										</v-toolbar>
										<v-card flat><HouseholdReviewSubmit v-bind:numberOfHouseholdMembers="getNumberOfHouseholdMembers()"/></v-card>				
									</v-stepper-content>
								</template>

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
				switch(this.page)
				{
					case config.registrationPages.GREETING_PAGE:
						if(this.$refs.greetingpage.verifyFormContents())
						{
							this.goToPage(config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE)
						}
						break;
					case config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE:
						this.$refs.householdregisternumber.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE) :
						this.goToPage(config.registrationPages.HOUSEHOLD_REGISTER_NUMBER_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE:
						this.$refs.householdhomeaddress.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_HOME_ADDRESS_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE:
						this.$refs.householdcontactinfo.verifyFormContents()?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_CONTACT_INFO_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE:
						this.$refs.householdPersonalInfo_1.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_1_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE:
						this.$refs.householdemergencycontact.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_EMERGENCY_CONTACT_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE: //patient #2
						this.$refs.householdPersonalInfo_2.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_REVIEW_SUBMIT_PAGE):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+1: //patient #3
						this.$refs.householdPersonalInfo_3.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+2):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+1);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+2: //patient #4
						this.$refs.householdPersonalInfo_4.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+3):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+2);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+3: //patient #5
						this.$refs.householdPersonalInfo_5.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+4):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+3);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+4: //patient #6
						this.$refs.householdPersonalInfo_6.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+5):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+4);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+5: //patient #7
						this.$refs.householdPersonalInfo_7.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+6):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+5);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+6: //patient #8
						this.$refs.householdPersonalInfo_8.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+7):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+6);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+7: //patient #9
						this.$refs.householdPersonalInfo_9.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+8):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+7);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+8: //patient #10
						this.$refs.householdPersonalInfo_10.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+9):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+8);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+9: //patient #11
						this.$refs.householdPersonalInfo_11.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+10):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+9);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+10: //patient #12
						this.$refs.householdPersonalInfo_12.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+11):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+10);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+11: //patient #13
						this.$refs.householdPersonalInfo_13.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+12):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+11);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+12: //patient #14
						this.$refs.householdPersonalInfo_14.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+13):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+12);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+13: //patient #15
						this.$refs.householdPersonalInfo_15.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+14):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+13);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+14: //patient #16
						this.$refs.householdPersonalInfo_16.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+15):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+14);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+15: //patient #17
						this.$refs.householdPersonalInfo_17.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+16):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+15);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+16: //patient #18
						this.$refs.householdPersonalInfo_18.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+17):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+16);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+17: //patient #19
						this.$refs.householdPersonalInfo_19.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+18):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+17);
						break;
					case config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+18: //patient #20
						this.$refs.householdPersonalInfo_20.verifyFormContents() ?
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+19):
						this.goToPage(config.registrationPages.HOUSEHOLD_PERSONAL_INFO_PATIENT_N_PAGE+18);
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
			numberOfHouseholdMembers: 2
			}
		},
  }
</script>