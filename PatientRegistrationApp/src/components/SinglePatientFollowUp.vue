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
    
    <v-row align="center"><div> How do you want to recieve this QR code? </div></v-row>
    <v-row no-gutters>
			<v-checkbox>
				<template #label>
				Email 
				</template>
			</v-checkbox>
    </v-row>
    <v-row>
      <v-checkbox>
				<template #label>
				SMS Message 
				</template>
			</v-checkbox>
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