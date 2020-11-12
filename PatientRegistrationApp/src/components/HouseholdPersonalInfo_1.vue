<template>
	<v-container fluid>
	<v-row align="center" justify="center">
		<!-- Last name -->
		<v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
			label="Last Name" 
			id="lastName" 
			required
			:rules="[v => !!v || 'Last name field is required']"
			v-model="householdFamilyName"
			prepend-icon="mdi-menu-right"
        ></v-text-field>
		</v-col>

		<!-- First name -->
		<v-col class="d-flex" cols="5" sm="5">
        <v-text-field 
			label="First Name" 
			id="firstName" 
			required
			:rules="[v => !!v || 'First name field is required']"
			v-model="householdGivenName">
        </v-text-field>
		</v-col>

		<!-- Suffix -->
		<v-col class="d-flex" cols="2" sm="2">
        <v-text-field 
			label="Suffix" 
			id="suffix" 
			v-model="householdSuffix">
        </v-text-field>
		</v-col>
	</v-row>

    <v-row>
		<v-col cols="12" sm="12" md="12">
        <v-checkbox
			v-model="checkbox"
			label="All household members have the same last name"
        ></v-checkbox>
		</v-col>
    </v-row>
	
    <v-row align="center" justify="center">
		<v-col class="d-flex" cols="5" sm="5">
        <!-- Date of Birth -->
        <v-menu
			ref="householdMenu"
			v-model="householdMenu"
			:close-on-content-click="false"
			transition="scale-transition"
			offset-y
			min-width="290px"
        >
		<template v-slot:activator="{ on, attrs }">
            <v-text-field
				v-model="householdDate"
				label="Date of Birth"
				prepend-icon="mdi-calendar"
				readonly
				v-bind="attrs"
				v-on="on"
            ></v-text-field>
		</template>

		<v-date-picker
            ref="picker"
            v-model="householdDate"
            :max="new Date().toISOString().substr(0, 10)"
            min="1900-01-01"
            @change="save"
		></v-date-picker>
        </v-menu>
		</v-col>

		<v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
		<v-col class="d-flex" cols="5" sm="5">
        <!-- Gender identity -->
        <v-select
			:items="genderID"
			label="Gender identity"
			required
			:rules="[v => !!v || 'Gender identity field is required']"
			v-model="householdGender"
			prepend-icon="mdi-menu-right"
        ></v-select>
	</v-col>

		<v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
		<v-col class="d-flex" cols="12" sm="12">
        <!-- Current Photo -->
        <v-file-input
			:rules="rules"
			accept="image/png, image/jpeg, image/bmp"
			placeholder="Upload a recent photo"
			v-model="householdPatientPhoto"
			label="Photo"
			prepend-icon="mdi-camera"
        ></v-file-input>
		</v-col>
    </v-row>

    <v-row align="center" justify="center">
		<v-col class="d-flex" cols="5" sm="5">
        <!-- Race -->
        <v-select
			v-model="householdRaceSelections"
			:items="race"
			label="Race (select all that apply)"
			prepend-icon="mdi-menu-right"
			multiple
        ></v-select>
		</v-col>
		<v-spacer></v-spacer>
    </v-row>

    <v-row align="center" justify="center">
		<v-col class="d-flex" cols="5" sm="5">
        <!-- Ethnicity -->
        <v-select
			v-model="householdEthnicitySelection"
			:items="ethnicity"
			label="Ethnicity"
			prepend-icon="mdi-menu-right"
        ></v-select>
		</v-col>
		<v-spacer></v-spacer>
	</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  data() {
    return {
      genderID: ["Male", "Female", "Other", "Decline to answer"],
      race: [
        "Black or African American",
        "White",
        "Asian",
        "American Indian or Alask a Native",
        "Native Hawaiian or other Pacific Islander",
        "Other",
      ],
      ethnicity: [
        "Hispanic or Latino",
        "Not Hispanic or Latino",
        "Unknown or prefer not to answer",
      ],
      householdFamilyName: '',
      householdGivenName: '',
      householdSuffix: '',
      householdBirthDate: '',
      householdGender: '',
      householdPatientPhoto: '',
      householdRaceSelections: '',
      householdEthnicitySelection: '',
    };
  },
  methods: {
    sendHouseholdPersonalInfoDataToReviewPage()
    {
      const householdPersonalInfoPayload = {
        householdFamilyName: this.householdFamilyName,
        householdGivenName: this.householdGivenName,
        householdSuffix: this.householdSuffix,
        householdBirthDate: this.householdDate,
        householdGender: this.householdGender,
        householdPatientPhoto: this.householdPatientPhoto,
        householdRaceSelections: this.householdRaceSelections,
        householdEthnicitySelection: this.householdEthnicitySelection
      }
      const householdMemberNumber = 1
      EventBus.$emit('DATA_HOUSEHOLD_PERSONAL_INFO_PUBLISHED', householdPersonalInfoPayload, householdMemberNumber)
    },
    verifyFormContents()
    {
      //add logic to check form contents
      var valid = true
      var message = "Woops! You need to enter the following field(s):"
      
      if(this.householdFamilyName == "")  
	  {
        message += " Last Name"
        valid = false
      }
      
      if(this.householdGivenName == "") 
	  {
	  if(!valid)
				{
				message +=","
				}
        message += " First Name"
        valid = false
      }
      
      
      if (this.householdDate == null) 
	  {
	  if(!valid)
				{
				message +=","
				}
        message += " Date of birth"
        valid = false
      }
      
      if(this.householdGender == "") 
	  {
	  if(!valid)
				{
				message +=","
				}
        message += " Gender Identity" 
        valid = false
      }
      
      if (valid == false) 
		{
          alert(message)
          return false
		}
      
        this.sendHouseholdPersonalInfoDataToReviewPage();
        return true;
    }
  },
};
</script>
