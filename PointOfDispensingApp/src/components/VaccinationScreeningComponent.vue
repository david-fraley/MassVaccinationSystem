<template>
  <v-container fill-height fluid>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">1. Are you feeling sick today?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ1" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row> 
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">2. Have you ever received a dose of COVID-19 Vaccine?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ2" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
      <!--blank column to indent this question-->
      <v-col cols="1">
      </v-col>
      <v-col cols="4">
        <v-row>
          <v-radio-group :disabled="(this.screeningQ2 != 'Yes')">
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="secondary--text">Which vaccine product did you receive?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="7">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ2b" 
            row 
            @change="screeningChecklistUpdate()" 
            :disabled="this.screeningQ2 != 'Yes' || isConsentScreeningPageReadOnly">
            <v-radio label="Pfizer" value="Pfizer" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="Moderna" value="Moderna" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="Other" value="Other" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="I don't know" value="I don't know" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="font-weight-medium secondary--text">3. Have you ever had an allergic reaction to:</span><br>
              <span style="font-size:.8rem">(This would include a severe allergic reaction [e.g. anaphylaxis] that required treatment with epinephrine 
              or EpiPen or that caused you to go to the hospital.  It would also include an allergic reaction that 
              occurred within 4 hours that caused hives, swelling, or respiratory distress, including wheezing.)</span></div>
            </template>
          </v-radio-group>
        </v-row>
        <!--needed to add a blank row for spacing: -->
        <v-row><p></p></v-row>
      </v-col>
      <!--blank column to indent this question-->
      <v-col cols="1">
      </v-col>
      <v-col cols="9">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="secondary--text">A component of the COVID-19 vaccine, including polyethylene 
              glycol (PEG), which is found in some medications, such as laxatives and preparations for colonoscopy procedures.</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ3a" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
      <!--blank column to indent this question-->
      <v-col cols="1">
      </v-col>
      <v-col cols="9">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="secondary--text">Polysorbate</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ3b" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
      <!--blank column to indent this question-->
      <v-col cols="1">
      </v-col>
      <v-col cols="9">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="secondary--text">A previous dose of COVID-19 vaccine</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ3c" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">4. Have you ever had an allergic reaction to another vaccine (other than COVID-19 vaccine) or an injectable medication?<br></span>
              <span style="font-size:.8rem">(This would include a severe allergic reaction [e.g. anaphylaxis] that required treatment with epinephrine or EpiPen or that caused you to go to the hospital.  
              It would also include an allergic reaction that occurred within 4 hours that caused hives, swelling, or respiratory distress, including wheezing.)</span></div>
            </template>
          </v-radio-group>
        </v-row>
        <!--needed to add a blank row for spacing: -->
        <v-row><p></p></v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ4" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">5. Have you ever had a severe allergic reaction (e.g., anaphylaxis) to something other than a component of COVID-19 vaccine, polysorbate, 
                or any vaccine or injectable medication? This would include food, pet, environmental, or oral medication allergies.</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ5" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">6. Have you received any vaccine in the last 14 days?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ6" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">7. Are you currently under isolation or quarantine due to COVID-19?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ7" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="10">
        <v-row>
          <v-radio-group>
            <template v-slot:label>
              <div><span class="red--text"><strong>* </strong></span>
              <span class="font-weight-medium secondary--text">8. Have you received passive antibody therapy or convalescent plasma as treatment for COVID-19 in the past 90 days?</span></div>
            </template>
          </v-radio-group>
        </v-row>
      </v-col>
      <v-col cols="2">
        <v-row align="center" justify="end">
          <v-radio-group 
            v-model="screeningQ8" 
            row 
            @change="screeningChecklistUpdate()"
            :disabled="isConsentScreeningPageReadOnly">
            <v-radio label="Yes" value="Yes" class="font-weight-medium secondary--text"></v-radio>
            <v-radio label="No" value="No" class="font-weight-medium secondary--text"></v-radio>
          </v-radio-group>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-divider></v-divider>
    </v-row>
      <v-col cols="12">
        <v-card class="mx-auto my-12" color="accent" dark height="50%" v-show="true">
          <v-card-title class="headline justify-center">
              Proceed with vaccination?
          </v-card-title>
          <v-row align="center" justify="center">
            <v-radio-group 
              v-model="vaccinationProceed" 
              row 
              @change="vaccinationProceedDecision()"
              :disabled="isConsentScreeningPageReadOnly">
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
  </v-container>
</template>

<script>
  //import brokerRequests from "../brokerRequests";
  export default {
    name: 'VaccinationScreeningComponent',
    computed: {
      isConsentScreeningPageReadOnly() {
        return this.$store.getters.isConsentScreeningPageReadOnly
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
       return !((this.screeningQ1 == "") || (this.screeningQ2 == "") || 
        ((this.screeningQ2 == 'Yes') && (this.screeningQ2b == '')) || 
        (this.screeningQ3a == "") || (this.screeningQ3b == "") || (this.screeningQ3c == "") || 
        (this.screeningQ4 == "") || (this.screeningQ5 == "") || (this.screeningQ6 == "") || 
        (this.screeningQ7 == "") || (this.screeningQ8 == "")); 
     },
     vaccinationProceedDecision()
     {
       this.storeVaccinationScreeningData()

      if(this.vaccinationProceed == 'Yes') {
        //send request to broker to update encounter status to "in-progress"
      }
      else if(this.vaccinationProceed == 'No') {
        //send request to broker to update encounter status to "cancelled"
      }
      console.log(this.vaccinationProceed)
     },
     onSuccess(payload) {
       //send data to Vuex
       console.log(payload)
       //this.$store.dispatch('encounterStatusUpdate', response.data)
    },
     storeVaccinationScreeningData()
     {
       const screeningResponsesPayload = {
        vaccinationDecision: this.vaccinationProceed,
        screeningQ1: this.screeningQ1,
        screeningQ2: this.screeningQ2,
        screeningQ2b: this.screeningQ2b,
        screeningQ3a: this.screeningQ3a,
        screeningQ3b: this.screeningQ3b,
        screeningQ3c: this.screeningQ3c,
        screeningQ4: this.screeningQ4,
        screeningQ5: this.screeningQ5,
        screeningQ6: this.screeningQ6,
        screeningQ7: this.screeningQ7,
        screeningQ8: this.screeningQ8,
        screeningComplete: this.screeningComplete
      }
      //send data to Vuex
      this.$store.dispatch('vaccinationScreeningUpdate', screeningResponsesPayload)
     },
     
    },
    components: 
    {
    },
    data () {
      return {
        vaccinationProceed: this.$store.state.screeningResponses.vaccinationDecision,
        screeningQ1: this.$store.state.screeningResponses.screeningQ1,
        screeningQ2: this.$store.state.screeningResponses.screeningQ2,
        screeningQ2b: this.$store.state.screeningResponses.screeningQ2b,
        screeningQ3a: this.$store.state.screeningResponses.screeningQ3a,
        screeningQ3b: this.$store.state.screeningResponses.screeningQ3b,
        screeningQ3c: this.$store.state.screeningResponses.screeningQ3c,
        screeningQ4: this.$store.state.screeningResponses.screeningQ4,
        screeningQ5: this.$store.state.screeningResponses.screeningQ5,
        screeningQ6: this.$store.state.screeningResponses.screeningQ6,
        screeningQ7: this.$store.state.screeningResponses.screeningQ7,
        screeningQ8: this.$store.state.screeningResponses.screeningQ8,
        screeningComplete: this.$store.state.screeningResponses.screeningComplete,
      }
    }
  }
</script>
<style lang="css" scoped>

.v-input--selection-controls {
  margin-top: 1px;
  padding-top: 1px;
}	
</style>
