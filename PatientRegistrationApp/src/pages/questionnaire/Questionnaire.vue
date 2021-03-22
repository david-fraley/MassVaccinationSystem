<template>
  <v-container>
    <v-form ref="form" v-model="formValid">
      <v-row align="center" justify="start">
        <v-col cols="12">
          <div>
            Information collected on this form will be used to document
            authorization for receipt of vaccines. Your responses will be
            verified when receiving the vaccine. The information will be shared
            through the Wisconsin Immunization Registry (WIR) with other health
            care providers directly involved with the patient to assure
            completion of the vaccine schedule. Information collected on this
            form is confidential.
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
                      <span class="red--text"><strong>* </strong></span>1. Are
                      you feeling sick today?
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ1"
                  @change="updateAnswers('screeningQ1', $event)"
                  row
                  :rules="questionRules"
                >
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
                  :value="answers.screeningQ2"
                  @change="updateAnswers('screeningQ2', $event)"
                  row
                  :rules="questionRules"
                >
                  <v-radio label="Yes" value="Yes"></v-radio>
                  <v-radio label="No" value="No"></v-radio>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col
              cols="12"
              sm="6"
              md="5"
              lg="4"
              v-show="answers.screeningQ2 == 'Yes'"
            >
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
            <v-col
              cols="12"
              sm="6"
              md="7"
              lg="8"
              v-show="answers.screeningQ2 == 'Yes'"
            >
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ2b"
                  @change="updateAnswers('screeningQ2b', $event)"
                  row
                  :rules="answers.screeningQ2 == 'Yes' ? questionRules : []"
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
                      hours that caused hives, swelling, or respiratory
                      distress, including wheezing.)
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
                <v-radio-group
                  :value="answers.screeningQ3a"
                  @change="updateAnswers('screeningQ3a', $event)"
                  row
                  :rules="questionRules"
                >
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
                <v-radio-group
                  :value="answers.screeningQ3b"
                  @change="updateAnswers('screeningQ3b', $event)"
                  row
                  :rules="questionRules"
                >
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
                      <span class="red--text"><strong>* </strong></span>A
                      previous dose of COVID-19 vaccines
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ3c"
                  @change="updateAnswers('screeningQ3c', $event)"
                  row
                  :rules="questionRules"
                >
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
                      you ever had an allergic reaction to another vaccine
                      (other than COVID-19 vaccine) or an injectable
                      medication?<br />
                      (This would include a severe allergic reaction [e.g.
                      anaphylaxis] that required treatment with epinephrine or
                      EpiPen or that caused you to go to the hospital. It would
                      also include an allergic reaction that occurred within 4
                      hours that caused hives, swelling, or respiratory
                      distress, including wheezing.)
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ4"
                  @change="updateAnswers('screeningQ4', $event)"
                  row
                  :rules="questionRules"
                >
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
                      you ever had a severe allergic reaction (e.g.,
                      anaphylaxis) to something other than a component of
                      COVID-19 vaccine, polysorbate, or any vaccine or
                      injectable medication? This would include food, pet,
                      environmental, or oral medication allergies.
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ5"
                  @change="updateAnswers('screeningQ5', $event)"
                  row
                  :rules="questionRules"
                >
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
                <v-radio-group
                  :value="answers.screeningQ6"
                  @change="updateAnswers('screeningQ6', $event)"
                  row
                  :rules="questionRules"
                >
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
                      <span class="red--text"><strong>* </strong></span>7. Are
                      you currently under isolation or quarantine due to
                      COVID-19?
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ7"
                  @change="updateAnswers('screeningQ7', $event)"
                  row
                  :rules="questionRules"
                >
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
                      you received passive antibody therapy or convalescent
                      plasma as treatment for COVID-19 in the past 90 days?
                    </div>
                  </template>
                </v-radio-group>
              </v-row>
            </v-col>
            <v-col cols="12" sm="4" md="3" lg="2">
              <v-row no-gutters align="center" justify="end">
                <v-radio-group
                  :value="answers.screeningQ8"
                  @change="updateAnswers('screeningQ8', $event)"
                  row
                  :rules="questionRules"
                >
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
      <!-- SUBMIT BUTTON -->
      <v-row>
        <v-col cols="12" sm="12" md="6">
          <v-alert
            dense
            outlined
            dismissible
            type="error"
            v-model="hasValidationErrors"
          >
            There are errors in the form, please fill out form completely.
          </v-alert>
        </v-col>
        <v-col cols="12">
          <v-btn
            clear
            color="secondary"
            class="ma-2 white--text"
            @click="back()"
          >
            <v-icon left large color="white"> mdi-chevron-left </v-icon>
            Back
          </v-btn>
          <v-btn
            color="primary"
            class="ma-2 white--text"
            @click="validateAndSubmit()"
          >
            Continue
            <v-icon right large color="white"> mdi-chevron-right </v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
export default {
  name: "Questionnaire",
  data() {
    return {
      formValid: false,
      hasValidationErrors: false,
      patientReceivedFirstDose: false,
      questionRules: [(v) => (v ? true : "You must choose one.")],
    };
  },
  computed: {
    answers() {
      return this.$store.state.screeningQuestions.answers;
    },
  },
  methods: {
    updateAnswers(qname, answer) {
      this.$store.commit("screeningQuestions/setQuestionAnswer", {
        qname,
        answer,
      });
    },
    validateAndSubmit() {
      this.$refs.form.validate();
      if (!this.formValid) {
        this.hasValidationErrors = true;
        return;
      }

      this.$router.push("/review")
    },
    back(){
      this.$router.push("/patient-info")
    }
  },
};
</script>

<style></style>
