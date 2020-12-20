<template>
	<v-container fluid>


    <v-row justify="center">
      QR Code Placeholder
    </v-row>

     <v-row justify="center">
      <div class="font-weight-medium">Name:  <span class="font-weight-regular">{{dataPersonalInfo.familyName}}, 
					{{dataPersonalInfo.givenName}} {{dataPersonalInfo.middleName}} {{dataPersonalInfo.suffix}}</span></div>
    </v-row>


    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text">
								Download
			</v-btn>
    </v-row>
    
 

<v-row justify="center"><v-col cols="12" sm="6" md="6">
      <v-radio-group class="font-weight-medium">
			How do you want to receive this QR code?
        <v-col cols="3" sm="3" md="3">
		
          <v-radio label="E-mail"></v-radio>
          <v-radio label="SMS Message"></v-radio>
          <v-radio label="Both"></v-radio>
        </v-col>
      </v-radio-group></v-col>
    </v-row>

    <v-row justify="center">
      <v-btn color="secondary" class="ma-2 white--text">
				Send
			</v-btn>
    </v-row>

		

	</v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  name: "SinglePatientFollowUp",
  data() {
    return {
      dataPersonalInfo:
			{
				familyName: '',
				givenName: '',
        suffix: '',
      },
      
    };
  },
  methods: {
    updatePersonalInfoData(personalInfoPayload) {
      this.dataPersonalInfo = personalInfoPayload
    },
},
  mounted() {
		EventBus.$on('DATA_PERSONAL_INFO_PUBLISHED', (personalInfoPayload) => {
			this.updatePersonalInfoData(personalInfoPayload)
    })
  }
}
</script>