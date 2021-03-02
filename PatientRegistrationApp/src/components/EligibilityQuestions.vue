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
    <v-row align="center" justify="start">
        <!-- County  -->
      <v-col cols="12" sm="6" md="6" lg="4">
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
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-form ref="form" v-model="valid">
        <!-- Date of Birth -->
        <v-text-field
          v-model="dob"
          :rules="birthdateRules"
          placeholder="MM/DD/YYYY"
          v-mask="'##/##/####'"
          prepend-icon="mdi-blank"
        >
          <template #label>
            <span class="red--text"><strong>* </strong></span>Date of Birth
          </template>
        </v-text-field>
        </v-form>
      </v-col>
    </v-row>
    <v-row no-gutters>
      <v-col cols="12">
        <v-row no-gutters>
          <v-divider></v-divider>
        </v-row>
        <v-row no-gutters>
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div><span class="red--text"><strong>* </strong></span>3. What is you current living status? (choose one)</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="12" md="12" lg="12">
            <v-row no-gutters align="center" justify="left">
              <v-radio-group v-model="eligibilityQ4" row required>
                <v-radio label="Nursing home / long-term care" value="nursingHome"></v-radio>
                <v-radio label="Shelter or other congregate care" value="shelter"></v-radio>
                <v-radio label="My own home with in-home care" value="inHomeCare"></v-radio>
                <v-radio label="Other (own home, apartment, etc.)" value="Other"></v-radio>
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
                  <div><span class="red--text"><strong>* </strong></span>4. Do you work in health care?</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="12" md="12" lg="12">
            <v-row no-gutters align="center" justify="left">
              <v-radio-group v-model="eligibilityQ4" row required>
                <v-radio label="Yes, I work in health care" value="Yes"></v-radio>
                <v-radio label="No, I do not work in health care" value="No"></v-radio>
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
                  <div><span class="red--text"><strong>* </strong></span>5. Do you provide services for an elderly person or a person with disabilities as a paid or unpaid caregiver? (Please select one option)</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="12" md="12" lg="12">
            <v-row no-gutters align="center" justify="left">
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
                  <div><span class="red--text"><strong>* </strong></span>6. What best describes your work? (Please select one option)</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="12" md="12" lg="12">
            <v-row no-gutters align="center" justify="left">
              <v-radio-group v-model="eligibilityQ6" row required>
                <v-radio label="Adult day facility worker" value="workPosition1"></v-radio>
                <v-radio label="Adult or older adult protective services" value="workPosition2"></v-radio>
                <v-radio label="Child care and early childhood program workers" value="workPosition3"></v-radio>
                <v-radio label="Child protective services" value="workPosition4"></v-radio>
                <v-radio label="Clergy and other essential support for houses of worship" value="workPosition5"></v-radio>
                <v-radio label="Communications" value="workPosition6"></v-radio>
                <v-radio label="Correctional officers and other workers serving people in congregate care settings not included in Phase 1A such as homeless shelters or rape crisis shelters" value="workPosition7"></v-radio>
                <v-radio label="Education workers" value="workPosition8"></v-radio>
                <v-radio label="Energy, including nuclear reactors" value="workPosition9"></v-radio>
                <v-radio label="Federal, state, county and local government workers, including county election workers, elected officials and members of the judiciary and their staff" value="workPosition10"></v-radio>
                <v-radio label="Finance, including bank tellers" value="workPosition11"></v-radio>
                <v-radio label="First responders" value="workPosition12"></v-radio>
                <v-radio label="Food and agricultural workers" value="workPosition13"></v-radio>
                <v-radio label="Food service" value="workPosition14"></v-radio>
                <v-radio label="Grocery store workers" value="workPosition15"></v-radio>
                <v-radio label="Housing construction" value="workPosition16"></v-radio>
                <v-radio label="Information technology" value="workPosition17"></v-radio>
                <v-radio label="Legal services" value="workPosition18"></v-radio>
                <v-radio label="Long-term care ombudsman" value="workPosition19"></v-radio>
                <v-radio label="Manufacturing workers" value="workPosition20"></v-radio>
                <v-radio label="Media" value="workPosition21"></v-radio>
                <v-radio label="Public health workers" value="workPosition22"></v-radio>
                <v-radio label="Public safety" value="workPosition23"></v-radio>
                <v-radio label="Public transit workers" value="workPosition24"></v-radio>
                <v-radio label="Retired/Not working" value="workPosition25"></v-radio>
                <v-radio label="Shelter and Housing" value="workPosition26"></v-radio>
                <v-radio label="Transportation and logistics" value="workPosition27"></v-radio>
                <v-radio label="Transportation and logistics - air or rail security" value="workPosition28"></v-radio>
                <v-radio label="U.S. Postal Service workers" value="workPosition29"></v-radio>
                <v-radio label="Water and wastewater" value="workPosition30"></v-radio>
                <v-radio label="Other" value="workPosition31"></v-radio>
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
                  <div><span class="red--text"><strong>* </strong></span>7. Do you have any of the following high-risk conditions? (Check all that apply)</div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" sm="12" md="12" lg="12">
            <v-row no-gutters align="center" justify="left">
              <v-radio-group v-model="eligibilityQ7" row required>
                <v-radio label="Cancer" value="condition1"></v-radio>
                <v-radio label="Chronic kidney disease" value="condition2"></v-radio>
                <v-radio label="COPD (chronic obstructive pulmonary disease)" value="condition3"></v-radio>
                <v-radio label="Down Syndrome" value="condition4"></v-radio>
                <v-radio label="Heart conditions, such as heart failure, coronary artery disease, or cardiomyopathies" value="condition5"></v-radio>
                <v-radio label="Immunocompromised state (weakened immune system) from solid organ transplant or from blood or bone marrow transplant, immune deficiencies, HIV, use of corticosteroids, or use of other immune weakening medicines" value="condition6"></v-radio>
                <v-radio label="Obesity (body mass index [BMI] of 30 kg/m2 or higher but < 40 kg/m2)" value="condition7"></v-radio>
                <v-radio label="Severe Obesity (BMI ≥ 40 kg/m2)" value="condition8"></v-radio>
                <v-radio label="Pregnancy" value="condition9"></v-radio>
                <v-radio label="Sickle cell disease" value="condition10"></v-radio>
                <v-radio label="Smoking" value="condition11"></v-radio>
                <v-radio label="Type 2 diabetes mellitus" value="condition12"></v-radio>
                <v-radio label="None of the above" value="condition13"></v-radio>
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
        county: '',
        dob: '',
		eligibilityQ3: '',
		eligibilityQ4: '',
		eligibilityQ5: '',
		eligibilityQ6: '',
		eligibilityQ7: '',
        minDateStr: "1900-01-01",
        birthdateRules: [
        (v) => !!v || "DOB is required",
        // check if v exists before seeing if the length is 10
        (v) =>
          (!!v && v.length === 10) ||
          "DOB must be in specified format, MM/DD/YYYY",
        (v) => this.validBirthdate(v) || "Invalid DOB",
      ],
      valid: false,
    };
  },
  computed: {
    maxDateStr: function() {
      let d = new Date();
      let date = [
        d.getFullYear(),
        ("0" + (d.getMonth() + 1)).slice(-2),
        ("0" + d.getDate()).slice(-2),
      ].join("-");

      return date;
    },
  },
  methods: {
    validBirthdate(birthdate) {
      var minDate = Date.parse(this.minDateStr);
      var maxDate = Date.parse(this.maxDateStr);
      var formattedDate = this.parseDate(birthdate);
      var date = Date.parse(formattedDate);

      return !Number.isNaN(date) && minDate <= date && date <= maxDate;
    },
    parseDate(date) {
      if (!date) return null;
      // Ensure birthdate is fully entered and can be converted into 3 variables before parsing
      if (date.split("/").length !== 3) return null;

      const [month, day, year] = date.split("/");
      return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
    },
    sendeligibilityResponsesToFollowUpPage() { //do we want to send it anywhere?
      const eligibilityResponsesPayload = {
        eligibilityQ3: this.eligibilityQ3,
        eligibilityQ4: this.eligibilityQ4,
        eligibilityQ5: this.eligibilityQ5,
        eligibilityQ6: this.eligibilityQ6,
        eligibilityQ7: this.eligibilityQ7,
      }
      EventBus.$emit('DATA_eligibility_RESPONSES_PUBLISHED', eligibilityResponsesPayload)
    },
    verifyFormContents() {
		var message = "Please complete all eligibility questions."
		
		if((this.eligibilityQ3 == "") || 
		(this.eligibilityQ4 == "") || (this.eligibilityQ5 == "") || (this.eligibilityQ6 == "") || 
		(this.eligibilityQ7 == "")) 
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