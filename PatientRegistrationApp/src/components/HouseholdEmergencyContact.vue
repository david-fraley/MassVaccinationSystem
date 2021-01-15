<template>
  <v-container fluid>
    <v-row align="center" justify="start">
    <!-- Last name -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-text-field
          required
          :rules="[v => !!v || 'Last name field is required']"
          v-model="emergencyContactFamilyName"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Last Name
          </template>
        </v-text-field>
      </v-col>
      <!-- First name -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-text-field 
          required
          :rules="[v => !!v || 'First name field is required']"
          v-model="emergencyContactGivenName"
          prepend-icon="mdi-blank">
          <template #label>
            <span class="red--text"><strong>* </strong></span>First Name
          </template>
        </v-text-field>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
      <!-- Phone Number -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-text-field
            required
            :rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
            v-mask="'(###)###-####'"
            v-model="emergencyContactPhoneNumber"
            prepend-icon="mdi-phone">
          <template #label>
            <span class="red--text"><strong>* </strong></span>Phone Number
          </template>
          </v-text-field>
      </v-col>
      <!-- Phone Number Type -->
      <v-col cols="12" sm="6" md="6" lg="4">
        <v-select
          v-model="emergencyContactPhoneNumberType"
          :items="phoneTypeOptions"
          label="Phone Type"
          prepend-icon="mdi-blank"
        ></v-select>
      </v-col>
    </v-row>
    <v-row align="center" justify="start">
			<!-- Relationship Type -->
			<v-col cols="12" sm="6" md="6" lg="4">
				<v-select
					v-model="emergencyContactRelationship"
					:items="relationshipOptions"
					label="Relationship"
					prepend-icon="mdi-blank">
				</v-select>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
  data() {
    return {
      phoneTypeOptions: ["Home", "Mobile", "Work"],
      relationshipOptions: ["Spouse", "Parent", "Guardian", "Care Giver", "Sibling", "Grandparent", "Child", "Foster Child", "Stepchild", "Other"],
      emergencyContactFamilyName: '',
      emergencyContactGivenName: '',
      emergencyContactPhoneNumber: '',
      emergencyContactPhoneNumberType: '',
      emergencyContactRelationship: ''
    };
  },
  methods: {
    sendHouseholdEmergencyContactInfoToReviewPage() {
      const householdEmergencyContactPayload = {
        emergencyContactFamilyName: this.emergencyContactFamilyName,
        emergencyContactGivenName: this.emergencyContactGivenName,
        emergencyContactPhoneNumber: this.emergencyContactPhoneNumber,
        emergencyContactPhoneNumberType: this.emergencyContactPhoneNumberType,
        emergencyContactRelationship: this.emergencyContactRelationship
      }
      EventBus.$emit('DATA_HOUSEHOLD_EMERGENCY_CONTACT_INFO_PUBLISHED', householdEmergencyContactPayload)
    },
    verifyFormContents() {
      var valid = true
      var message = "Woops! You need to enter the following field(s):"
      
      if(this.emergencyContactFamilyName == "") {
        message += " Last Name"
        valid = false
      }
      if(this.emergencyContactGivenName == "") {
        if(!valid) {
          message +=","
				}
        message += " First Name"
        valid = false
      }
      if (this.emergencyContactPhoneNumber == "") {
        if(!valid) {
          message +=","
				}
        message += " Phone Number"
        valid = false
      }
      if (valid == false) {
        alert(message)
        return false
      }
      this.sendHouseholdEmergencyContactInfoToReviewPage();
      return true;
    }
  },
};
</script>


