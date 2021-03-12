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
          <div><span class="red--text"><strong>* </strong></span>1. Which county do you live in?</div></v-col>
    </v-row>
    <v-row no-gutters>
          <v-col cols="4">
          <v-text-field
            dense
            required
            placeholder="County"
          :rules="[(v) => !!v || 'County field is required']"
          v-model="county"
          prepend-icon="mdi-blank"
          ></v-text-field></v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row> 
    <!-- Eligibility Question 2 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>2. What is your current living status?</div>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-radio-group v-model="eligibilityQ2" row required>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Nursing home / long-term care" value="nursingHome"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Shelter or other congregate care" value="shelter"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="My own home with in-home care" value="inHomeCare"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Other (own home, apartment, etc.)" value="Other"></v-radio></v-col>
        </v-radio-group>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
    <!-- Eligibility Question 3 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>3. Do you work in health care?</div>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-radio-group v-model="eligibilityQ3" row required>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Yes, I work in health care" value="Yes"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="No, I do not work in health care" value="No"></v-radio></v-col>
        </v-radio-group>
      </v-col>
    </v-row>   
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
    <!-- Eligibility Question 4 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>4. Do you provide services for an elderly person or a person with disabilities as a paid or unpaid caregiver?</div>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-radio-group v-model="eligibilityQ4" row required>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Yes" value="Yes"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="No" value="No"></v-radio></v-col>
        </v-radio-group>
      </v-col>
    </v-row> 
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
    <!-- Eligibility Question 5 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>5. What best describes your work?</div>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-radio-group v-model="eligibilityQ5" row required>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Adult day facility worker"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Adult or older adult protective services"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Childcare and early childhood program workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Child protective services"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Clergy and other essential support for houses of worship"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Communications"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Correctional officers and other workers serving people in congregate care settings not included in Phase 1A such as homeless shelters or rape crisis shelters"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Education workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Energy, including nuclear reactors"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Federal, state, county, and local government workers, including county election workers, elected officials and members of the judiciary and their staff"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Finance, including bank tellers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="First responders"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Food and agricultural workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Food service"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Grocery store workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Housing construction"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Information technology"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Legal services"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Long-term care ombudsman"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Manufacturing workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Media"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public health workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public safety"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public transit workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Retired/Not working"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Shelter and Housing"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Transportation and logistics"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Transportation and logistics - air or rail security"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="U.S. Postal Service workers"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Water and wastewater"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Other"></v-radio></v-col>
        </v-radio-group>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
    <!-- Eligibility Question 6 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>6. Do you have any of the following high-risk conditions? (Check all that apply)</div>
    </v-row>
    <v-row no-gutters>
        <!-- <v-radio-group v-model="eligibilityQ6" row required> -->
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Cancer" value="condition1"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Chronic kidney disease" value="condition2"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="COPD (chronic obstructive pulmonary disease)" value="condition3"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Down Syndrome" value="condition4"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies" value="condition5"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Immunocompromised state (weakened immune system) from solid organ transplant or from blood or bone marrow transplant, immune deficiencies, HIV, use of corticosteroids, or use of other immune weakening medicines" value="condition6"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Obesity (body mass index [BMI] of 30 kg/m2 or higher but < 40 kg/m2)" value="condition7"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Severe Obesity (BMI ≥ 40 kg/m2)" value="condition8"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Pregnancy" value="condition9"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Sickle cell disease" value="condition10"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Smoking" value="condition11"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" :disabled="enabled" label="Type 2 diabetes mellitus" value="condition12"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="enabled" label="None of the above"></v-checkbox></v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row>
  </v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  name: "eligibilityQuestions",
  data() {
    return {
        county: '',
		eligibilityQ2: '',
		eligibilityQ3: '',
		eligibilityQ4: '',
		eligibilityQ5: '',
		eligibilityQ6: [],
    valid: false,
    enabled: false,
    };
  },
  computed: {
  },
  methods: {
    sendeligibilityResponsesToReviewPage() { //do we want to send it anywhere?
      const eligibilityResponsesPayload = {
        county: this.county,
        eligibilityQ3: this.eligibilityQ2,
        eligibilityQ4: this.eligibilityQ3,
        eligibilityQ5: this.eligibilityQ4,
        eligibilityQ6: this.eligibilityQ5,
        eligibilityQ7: this.eligibilityQ6,
      }
      EventBus.$emit('DATA_eligibility_RESPONSES_PUBLISHED', eligibilityResponsesPayload)
    },
    verifyFormContents() {
		var message = "Please complete all eligibility questions."
		
		if((this.county == "" || this.eligibilityQ2 == "") || 
		(this.eligibilityQ3 == "") || (this.eligibilityQ4 == "") || (this.eligibilityQ5 == "") || 
		(this.eligibilityQ6 == "")) 
		{
			alert(message)
			return false
		}
		this.sendeligibilityResponsesToReviewPage();
		return true;
    }
},
};
</script>
