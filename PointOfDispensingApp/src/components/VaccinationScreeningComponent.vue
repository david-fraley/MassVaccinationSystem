<template>
  <v-container fill-height fluid>
    <v-row no-gutters>
      <v-col cols="6">
        <v-row no-gutters>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>Patient info confirmed? </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group v-model="patientInfoComfirmed" row @change="screeningChecklistUpdate()">
                  <v-radio
                    label="Yes"
                    value="Yes"
                  ></v-radio>
                  <v-radio
                    label="No"
                    value="No"
                  ></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row> 
        <v-row no-gutters>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>Consent form signed?</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group v-model="consentFormSigned" row @change="screeningChecklistUpdate()">
                  <v-radio
                    label="Yes"
                    value="Yes"
                  ></v-radio>
                  <v-radio
                    label="No"
                    value="No"
                  ></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>Proper screening completed?</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group v-model="screeningCompleted" row @change="screeningChecklistUpdate()">
                  <v-radio
                    label="Yes"
                    value="Yes"
                  ></v-radio>
                  <v-radio
                    label="No"
                    value="No"
                  ></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>VIS fact sheet provided?</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="6">
            <v-row no-gutters>
              <v-radio-group v-model="factSheetProvided" row @change="screeningChecklistUpdate()">
                  <v-radio
                    label="Yes"
                    value="Yes"
                  ></v-radio>
                  <v-radio
                    label="No"
                    value="No"
                  ></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
      </v-col>
                
      <v-col cols="6">
          <v-card class="mx-auto my-12" color="accent" dark height="50%" v-show="screeningComplete">
            <v-card-title class="headline justify-center">
                Proceed with vaccination?
            </v-card-title>
            <v-row align="center" justify="center">
              <v-radio-group v-model="vaccinationProceed" row @change="vaccinationProceedDecision()">
                    <v-radio
                      label="Yes"
                      value="Yes"
                    ></v-radio>
                    <v-radio
                      label="No"
                      value="No"
                    ></v-radio>
                </v-radio-group>
            </v-row>
          </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

  export default {
    name: 'VaccinationScreeningComponent',
    computed:
    {
      patientInfoConfirmedResponse() {
        return this.$store.state.screeningResponses.patientInfoConfirmed
      },
      consentFormSignedResponse() {
        return this.$store.state.screeningResponses.consentFormSigned
      },
      screeningCompletedResponse() {
        return this.$store.state.screeningResponses.screeningCompleted
      },
      factSheetProvidedResponse() {
        return this.$store.state.screeningResponses.factSheetProvided
      },
    },
    methods: 
    {
     screeningChecklistUpdate()
     {
       if(this.isScreeningChecklistComplete())
       {
         this.screeningComplete = true
       }
       else
       {
         this.screeningComplete = false
       }
       this.storeVaccinationScreeningData()
     },
     isScreeningChecklistComplete()
     {
       return (this.patientInfoComfirmed && this.consentFormSigned && this.screeningCompleted && this.factSheetProvided); 
     },
     vaccinationProceedDecision()
     {
       if(this.vaccinationProceed == "Yes")
       {
         this.$emit('VaccinationProceed_Yes')
       }
       else
       {
         this.$emit('VaccinationProceed_No')
       }
       this.storeVaccinationScreeningData()
     },
     storeVaccinationScreeningData()
     {
       const screeningResponsesPayload = {
        vaccinationDecision: this.vaccinationProceed,
        patientInfoConfirmed: this.patientInfoComfirmed,
        consentFormSigned: this.consentFormSigned,
        screeningCompleted: this.screeningCompleted,
        factSheetProvided: this.factSheetProvided,
        screeningComplete: this.screeningComplete
      }
      //send data to Vuex
      this.$store.dispatch('vaccinationScreeningUpdate', screeningResponsesPayload)
     }
    },
    components: 
    {
    },
    data () {
      return {
        vaccinationProceed: this.$store.state.screeningResponses.vaccinationDecision,
        patientInfoComfirmed: this.$store.state.screeningResponses.patientInfoConfirmed,
        consentFormSigned: this.$store.state.screeningResponses.consentFormSigned,
        screeningCompleted: this.$store.state.screeningResponses.screeningCompleted,
        factSheetProvided: this.$store.state.screeningResponses.factSheetProvided,
        screeningComplete: this.$store.state.screeningResponses.screeningComplete,
      }
    }
  }
</script>
