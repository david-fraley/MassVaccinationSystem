<template>
  <v-container fill-height fluid>
    <v-row align="center" justify="start">
      <v-col cols="12">
        <div>Eligibility Questions: Information collected on this form will be used to document authorization for receipt of vaccines.  
          Your responses will be verified when receiving the vaccine.  The information will be shared through the 
          Wisconsin Immunization Registry (WIR) with other health care providers directly involved with the 
          patient to assure completion of the vaccine schedule.  Information collected on this form is confidential.
        </div>
        <div><br></div>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-row no-gutters>
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>1. Question 1</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ1" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>2. Question 2</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
         <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ2" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>2. Question 3</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ3" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>4. Question 4</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ4" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>5. Question 5</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ5" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>6. Question 6</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ6" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>7. Question 7</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ7" row required>
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
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>8. Question 8</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="eligibilityQ8" row required>
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
  </v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  name: "eligibilityQuestions",
  data() {
    return {
		eligibilityQ1: '',
		eligibilityQ2: '',
		eligibilityQ2b: '',
		eligibilityQ3a: '',
		eligibilityQ3b: '',
		eligibilityQ3c: '',
		eligibilityQ4: '',
		eligibilityQ5: '',
		eligibilityQ6: '',
		eligibilityQ7: '',
		eligibilityQ8: '',
    };
  },
  methods: {
    sendeligibilityResponsesToFollowUpPage() { //do we want to send it anywhere?
      const eligibilityResponsesPayload = {
        eligibilityQ1: this.eligibilityQ1,
        eligibilityQ2: this.eligibilityQ2,
        eligibilityQ2b: this.eligibilityQ2b,
        eligibilityQ3a: this.eligibilityQ3a,
        eligibilityQ3b: this.eligibilityQ3b,
        eligibilityQ3c: this.eligibilityQ3c,
        eligibilityQ4: this.eligibilityQ4,
        eligibilityQ5: this.eligibilityQ5,
        eligibilityQ6: this.eligibilityQ6,
        eligibilityQ7: this.eligibilityQ7,
        eligibilityQ8: this.eligibilityQ8,
      }
      EventBus.$emit('DATA_eligibility_RESPONSES_PUBLISHED', eligibilityResponsesPayload)
    },
    verifyFormContents() {
		var message = "Please complete all eligibility questions."
		
		if((this.eligibilityQ1 == "") || (this.eligibilityQ2 == "") || 
		((this.eligibilityQ2 == 'Yes') && (this.eligibilityQ2b == '')) || 
		(this.eligibilityQ3a == "") || (this.eligibilityQ3b == "") || (this.eligibilityQ3c == "") || 
		(this.eligibilityQ4 == "") || (this.eligibilityQ5 == "") || (this.eligibilityQ6 == "") || 
		(this.eligibilityQ7 == "") || (this.eligibilityQ8 == "")) 
		{
			alert(message)
			return false
		}
		this.sendeligibilityResponsesToFollowUpPage();
		return true;
    }
},
};
</script>