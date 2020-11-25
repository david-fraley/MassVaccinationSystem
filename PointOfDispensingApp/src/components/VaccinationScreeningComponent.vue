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
              <v-radio-group v-model="patientInfoComfirmed" row>
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
              <v-radio-group v-model="consentFormSigned" row>
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
              <v-radio-group v-model="screeningCompleted" row>
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
              <v-radio-group v-model="factSheetProvided" row>
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
        <v-card color="accent" dark height="50%" v-show="true">
          <v-card-title class="headline justify-center">
              Proceed with vaccination?
          </v-card-title>
          <v-row align="center" justify="center">
            <v-col cols="2">
                <v-radio
                  v-model="vaccinationYes"
                  label="Yes"
                  value="Yes"
                ></v-radio>
            </v-col>
            <v-col cols="2">   
                <v-radio
                  v-model="vaccinationNo"
                  label="No"
                  value="No"
                ></v-radio>
            </v-col>
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
    methods: 
    {
    },
    components: 
    {
    },
    data () {
      return {
      }
    }
  }
</script>
