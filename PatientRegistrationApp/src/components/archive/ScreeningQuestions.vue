<template>
  <v-container fill-height fluid>
    <v-row align="center" justify="start">
      <v-col cols="12">
        <div>
          Information collected on this form will be used to document
          authorization for receipt of vaccines. Your responses will be verified
          when receiving the vaccine. The information will be shared through the
          Wisconsin Immunization Registry (WIR) with other health care providers
          directly involved with the patient to assure completion of the vaccine
          schedule. Information collected on this form is confidential.
        </div>
        <div><br /></div>
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>1. Are you
                    feeling sick today?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ1" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>2. Have
                    you ever received a dose of COVID-19 Vaccine?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group
                v-model="screeningQ2"
                row
                :rules="questionRules"
                @change="FirstDoseResponse()"
              >
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="6" md="5" lg="4" v-show="patientReceivedFirstDose">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>
                    <span class="red--text"><strong>* </strong></span>Which
                    vaccine product did you receive?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="6" md="7" lg="8" v-show="patientReceivedFirstDose">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group
                v-model="screeningQ2b"
                row
                :rules="patientReceivedFirstDose ? questionRules : true"
              >
                <v-radio label="Pfizer" value="Pfizer"></v-radio>
                <v-radio label="Moderna" value="Moderna"></v-radio>
                <v-radio label="Other" value="Other"></v-radio>
                <v-radio label="I don't know" value="I don't know"></v-radio>
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
                  <div>
                    3. Have you ever had an allergic reaction to:<br />
                    (This would include a severe allergic reaction [e.g.
                    anaphylaxis] that required treatment with epinephrine or
                    EpiPen or that caused you to go to the hospital. It would
                    also include an allergic reaction that occurred within 4
                    hours that caused hives, swelling, or respiratory distress,
                    including wheezing.)
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>
                    <span class="red--text"><strong>* </strong></span>A
                    component of the COVID-19 vaccine, including polyethylene
                    glycol (PEG), which is found in some medications, such as
                    laxatives and preparations for colonoscopy procedures.
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ3a" row :rules="questionRules">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>
                    <span class="red--text"><strong>* </strong></span
                    >Polysorbate
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ3b" row :rules="questionRules">
                <v-radio label="Yes" value="Yes"></v-radio>
                <v-radio label="No" value="No"></v-radio>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="8" md="9" lg="10">
            <v-row no-gutters>
              <v-radio-group>
                <template v-slot:label>
                  <div>
                    <span class="red--text"><strong>* </strong></span>A previous
                    dose of COVID-19 vaccines
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ3c" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>4. Have
                    you ever had an allergic reaction to another vaccine (other
                    than COVID-19 vaccine) or an injectable medication?<br />
                    (This would include a severe allergic reaction [e.g.
                    anaphylaxis] that required treatment with epinephrine or
                    EpiPen or that caused you to go to the hospital. It would
                    also include an allergic reaction that occurred within 4
                    hours that caused hives, swelling, or respiratory distress,
                    including wheezing.)
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ4" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>5. Have
                    you ever had a severe allergic reaction (e.g., anaphylaxis)
                    to something other than a component of COVID-19 vaccine,
                    polysorbate, or any vaccine or injectable medication? This
                    would include food, pet, environmental, or oral medication
                    allergies.
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ5" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>6. Have
                    you received any vaccine in the last 14 days?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ6" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>7. Are you
                    currently under isolation or quarantine due to COVID-19?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ7" row :rules="questionRules">
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
                  <div>
                    <span class="red--text"><strong>* </strong></span>8. Have
                    you received passive antibody therapy or convalescent plasma
                    as treatment for COVID-19 in the past 90 days?
                  </div>
                </template>
              </v-radio-group>
            </v-row>
          </v-col>
          <v-col cols="12" sm="4" md="3" lg="2">
            <v-row no-gutters align="center" justify="end">
              <v-radio-group v-model="screeningQ8" row :rules="questionRules">
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

export default {
  name: "ScreeningQuestions",
  data() {
    return {
      screeningQ1: "",
      screeningQ2: "",
      screeningQ2b: "",
      screeningQ3a: "",
      screeningQ3b: "",
      screeningQ3c: "",
      screeningQ4: "",
      screeningQ5: "",
      screeningQ6: "",
      screeningQ7: "",
      screeningQ8: "",
      patientReceivedFirstDose: false,
      hasValidationErrors: false,
      questionRules: [v => v ? true : 'You must choose one.'],
    };
  },
  methods: {
    FirstDoseResponse() {
      this.patientReceivedFirstDose = this.screeningQ2 == "Yes";
      if (!this.patientReceivedFirstDose) {
        this.screeningQ2b = "";
      }
    },
    sendScreeningResponsesToFollowUpPage() {
      const screeningResponsesPayload = {
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
      };
      this.$emit(
        "DATA_SCREENING_RESPONSES_PUBLISHED",
        screeningResponsesPayload
      );
    },
    verifyFormContents() {
      var message = "Please complete all screening questions.";

      if (
        this.screeningQ1 == "" ||
        this.screeningQ2 == "" ||
        (this.screeningQ2 == "Yes" && this.screeningQ2b == "") ||
        this.screeningQ3a == "" ||
        this.screeningQ3b == "" ||
        this.screeningQ3c == "" ||
        this.screeningQ4 == "" ||
        this.screeningQ5 == "" ||
        this.screeningQ6 == "" ||
        this.screeningQ7 == "" ||
        this.screeningQ8 == ""
      ) {
        alert(message);
        return false;
      }
      this.sendScreeningResponsesToFollowUpPage();
      return true;
    },
  },
};
</script>
