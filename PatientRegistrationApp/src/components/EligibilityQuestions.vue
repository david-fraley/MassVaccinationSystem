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
    <v-row align="center" justify="center">
        <!-- County  -->
      <v-col cols="12" sm="12" md="6" lg="6">
        <v-text-field
          required
          :rules="[(v) => !!v || 'County field is required']"
          v-model="county"
          prepend-icon="mdi-blank"
        >
          <template #label>
            <span class="red--text"><strong>* </strong></span>County
          </template>
        </v-text-field>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-divider></v-divider>
    </v-row> 
    <!-- Eligibility Question 2 -->
    <v-row no-gutters>
          <div><span class="red--text"><strong>* </strong></span>2. What is your current living status? (choose one)</div>
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
          <div><span class="red--text"><strong>* </strong></span>4. Do you provide services for an elderly person or a person with disabilities as a paid or unpaid caregiver? (Please select one option)</div>
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
          <div><span class="red--text"><strong>* </strong></span>5. What best describes your work? (Please select one option)</div>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-radio-group v-model="eligibilityQ5" row required>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Adult day facility worker" value="workPosition1"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Adult or older adult protective services" value="workPosition2"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Childcare and early childhood program workers" value="workPosition3"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Child protective services" value="workPosition4"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Clergy and other essential support for houses of worship" value="workPosition5"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Communications" value="workPosition6"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Correctional officers and other workers serving people in congregate care settings not included in Phase 1A such as homeless shelters or rape crisis shelters" value="workPosition7"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Education workers" value="workPosition8"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Energy, including nuclear reactors" value="workPosition9"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Federal, state, county, and local government workers, including county election workers, elected officials and members of the judiciary and their staff" value="workPosition10"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Finance, including bank tellers" value="workPosition11"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="First responders" value="workPosition12"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Food and agricultural workers" value="workPosition13"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Food service" value="workPosition14"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Grocery store workers" value="workPosition15"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Housing construction" value="workPosition16"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Information technology" value="workPosition17"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Legal services" value="workPosition18"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Long-term care ombudsman" value="workPosition19"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Manufacturing workers" value="workPosition20"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Media" value="workPosition21"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public health workers" value="workPosition22"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public safety" value="workPosition23"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Public transit workers" value="workPosition24"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Retired/Not working" value="workPosition25"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Shelter and Housing" value="workPosition26"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Transportation and logistics" value="workPosition27"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Transportation and logistics - air or rail security" value="workPosition28"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="U.S. Postal Service workers" value="workPosition29"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Water and wastewater" value="workPosition30"></v-radio></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-radio label="Other" value="workPosition31"></v-radio></v-col>
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
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Cancer" value="condition1"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Chronic kidney disease" value="condition2"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="COPD (chronic obstructive pulmonary disease)" value="condition3"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Down Syndrome" value="condition4"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies" value="condition5"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Immunocompromised state (weakened immune system) from solid organ transplant or from blood or bone marrow transplant, immune deficiencies, HIV, use of corticosteroids, or use of other immune weakening medicines" value="condition6"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Obesity (body mass index [BMI] of 30 kg/m2 or higher but < 40 kg/m2)" value="condition7"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Severe Obesity (BMI ≥ 40 kg/m2)" value="condition8"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Pregnancy" value="condition9"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Sickle cell disease" value="condition10"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Smoking" value="condition11"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="Type 2 diabetes mellitus" value="condition12"></v-checkbox></v-col>
          <v-col cols="12" sm="12" md="6" lg="6"><v-checkbox v-model="eligibilityQ6" label="None of the above" value="condition13"></v-checkbox></v-col>
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
		eligibilityQ6: [''],
      valid: false,
    };
  },
  computed: {
      rules() {
      return [
        this.selected.length > 0 || "At least one item should be selected"
      ];
    }
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
