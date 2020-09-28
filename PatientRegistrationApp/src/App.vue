<template>

  <v-app id="Patient-registration">
  
    <v-main>
		<v-container>
			
			<v-card 
				class="elevation-12"
				min-width="400"
				max-width="1000"
			>
				
				<v-stepper 
					v-model="page"
				>
				
					<v-stepper-header>
					
						<!-- Greeting Page -->
						<v-stepper-step
							:complete="page > 1"
							step="1"
						></v-stepper-step>
						
						<v-divider></v-divider>
						
						<!-- Home Address -->
						<v-stepper-step
							:complete="page > 2"
							step="2"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Contact Info -->
						<v-stepper-step
							:complete="page > 3"
							step="3"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Personal Info -->
						<v-stepper-step
							:complete="page > 4"
							step="4"
						></v-stepper-step>

						<v-divider></v-divider>
						
						<!-- Emergency Contact -->
						<v-stepper-step
							:complete="page > 5"
							step="5"
						></v-stepper-step>

						<v-divider></v-divider>

						<!-- Review and Submit -->
						<v-stepper-step step="6">
						</v-stepper-step>
					</v-stepper-header>
					
					<v-toolbar
						color="primary"
						dark
						flat
					>
					<v-toolbar-title>COVID-19 Vaccination Registration</v-toolbar-title>
					</v-toolbar>			
					
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
							<v-card
								flat
							>
								<!-- placeholder -->
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
									@click="page=3"
								>
									mdi-chevron-right
								</v-icon>
							</v-card-actions>
						</v-stepper-content>
						
						<!-- Contact Info -->
						<v-stepper-content step="3">
							<v-card
								flat
							>
								<!-- placeholder -->
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
							<v-card
								flat
							>
								<!--placeholder-->
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
							<v-card
								flat
							>
								<!-- placeholder -->
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
							<v-card
								flat
							>
								<!-- placeholder -->
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


export default {
	name: 'App',
	methods: 
	{
		next() 
		{
			alert('You clicked next!')
		},
		previous() 
		{
			alert('You clicked previous!')
		},
		submit()
		{
			alert('You clicked submit!')
		}	
	},
  components: 
  {
	GreetingPage,
  },

  data () {
	return {
		page: 1,
		}
	},
  }

</script>
