<template>

  <v-app id="Patient-registration">
 
    <v-main>
		<v-container bg fill-height grid-list-md text-xs-center>
			<v-row
				align="center"
				justify="center"
			>
				<v-col
					cols="12"
				>
					<v-card 
						class="elevation-12" 
						min-width="400"
						max-width="1000"
					> 
						<v-toolbar
							color="blue darken-4"
							
						>
						<v-toolbar-title primary-title class="grey--text text--lighten-5">COVID-19 Vaccination Registration</v-toolbar-title>
						</v-toolbar>						
						<v-form class ="grey lighten-5">
							<GreetingPage/>
						</v-form>
						
						<v-card-actions> 
							<v-icon
								large
								color="#FDD835"
								@click="previous"
							>
								mdi-chevron-left
							</v-icon>
							
							<v-spacer></v-spacer>
							
							<v-icon
								large
								color="#FDD835"
								@click="next"
							>
								mdi-chevron-right
							</v-icon>
						</v-card-actions>
					</v-card>
				</v-col>
			</v-row>
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
		}
	},
  components: 
  {
    GreetingPage,
  },

  data: () => ({
    //
  }),
};
</script>
