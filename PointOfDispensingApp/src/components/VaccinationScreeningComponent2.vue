<template>
  <v-container fill-height fluid>
    <v-row no-gutters>
      <v-col cols="12">
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>1. Are you feeling sick today? Mild illnesses (eg upper respiratory, infections) are NOT contraindications to vaccination.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ1" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row> 
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>2. Have you ever received a dose of COVID-19 Vaccine?  If so, confirm manufacturer.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ2" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>3. Have you ever had an allergic reaction to any of the following?  If so, no vaccination. **include list**</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ3" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>4. Have you ever had an allergic reaction to another vaccine (other than COVID-19 vaccine) or an injectable medication?</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ4" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>5. Have you ever had a severe allergic reaction (e.g., anaphylaxis) to something other than a component of COVID-19 vaccine, polysorbate, or any vaccine or injectable medication? ...</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ5" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>6. Have you received any vaccine in the last 14 days? If yes, NO vaccination.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ6" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>7. Have you ever had a positive test for COVID-19 or has a doctor ever told you that you had COVID-19? No Vaccine until patient is outside of the quarantine window.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ7" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>8. Have you received passive antibody therapy (monoclonal antibodies or convalescent serum) as treatment for COVID-19? If yes, vaccination should be delayed 90 days post treatment.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ8" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>9. Do you have a weakened immune system caused by something such as HIV infection or cancer or do you take immunosuppressive drugs or therapies? If yes, patient should consult their Dr. prior to obtaining vaccination.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ9" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>10. Do you have a bleeding disorder or are you taking a blood thinner? If yes, apply direct pressure for 2 minutes</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ10" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>11. Are you pregnant or breastfeeding? If yes, patient should consult their Dr. prior to obtaining vaccination.</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="2">
            <v-row no-gutters align="center" justify="center">
              <v-radio-group v-model="screeningQ11" row @change="screeningChecklistUpdate()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
      </v-col>
    </v-row>
    <v-row no-gutters>      
      <v-col cols="12">
          <v-card class="mx-auto my-12" color="accent" dark height="50%" v-show="screeningComplete">
            <v-card-title class="headline justify-center">
                Proceed with vaccination?
            </v-card-title>
            <v-row align="center" justify="center">
              <v-radio-group v-model="vaccinationProceed" row @change="vaccinationProceedDecision()" :disabled="isVaccinationEventPageReadOnly">
                <v-radio label="Yes" value="Yes"></v-radio>
                 <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

  export default {
    name: 'VaccinationScreeningComponent',
    computed:
    {
      isVaccinationEventPageReadOnly() {
        return this.$store.getters.isVaccinationEventPageReadOnly
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
       return (this.screeningQ1 && this.screeningQ2 && this.screeningQ3 && this.screeningQ4 && this.screeningQ5 && this.screeningQ6
              && this.screeningQ7 && this.screeningQ8 && this.screeningQ9 && this.screeningQ10 && this.screeningQ11); 
     },
     vaccinationProceedDecision()
     {
       this.storeVaccinationScreeningData()
     },
     storeVaccinationScreeningData()
     {
       const screeningResponsesPayload = {
        vaccinationDecision: this.vaccinationProceed,
        screeningQ1: this.screeningQ1,
        screeningQ2: this.screeningQ2,
        screeningQ3: this.screeningQ3,
        screeningQ4: this.screeningQ4,
        screeningQ5: this.screeningQ5,
        screeningQ6: this.screeningQ6,
        screeningQ7: this.screeningQ7,
        screeningQ8: this.screeningQ8,
        screeningQ9: this.screeningQ9,
        screeningQ10: this.screeningQ10,
        screeningQ11: this.screeningQ11,
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
        screeningQ1: this.$store.state.screeningResponses.screeningQ1,
        screeningQ2: this.$store.state.screeningResponses.screeningQ2,
        screeningQ3: this.$store.state.screeningResponses.screeningQ3,
        screeningQ4: this.$store.state.screeningResponses.screeningQ4,
        screeningQ5: this.$store.state.screeningResponses.screeningQ5,
        screeningQ6: this.$store.state.screeningResponses.screeningQ6,
        screeningQ7: this.$store.state.screeningResponses.screeningQ7,
        screeningQ8: this.$store.state.screeningResponses.screeningQ8,
        screeningQ9: this.$store.state.screeningResponses.screeningQ9,
        screeningQ10: this.$store.state.screeningResponses.screeningQ10,
        screeningQ11: this.$store.state.screeningResponses.screeningQ11,
        screeningComplete: this.$store.state.screeningResponses.screeningComplete,
      }
    }
  }
</script>
