<template>
	<v-container fluid>
		<v-row>
			<v-col cols="12" sm="6" md="3">
				<v-text-field
					required
					:rules="[v => !!v || 'Phone number is required']"
					label="Primary Phone Number"
					v-model="primaryPhoneNumber"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="6" sm="2">
				<v-select
					required
					:rules="[v => !!v || 'Phone type is required']"
					v-model="primaryPhoneNumberType"
					:items="phonetype"
					label="Phone Type"
				></v-select>
			</v-col>
			<v-col cols="12" sm="6" md="3">
				<v-text-field
					label="Secondary Phone Number"
					v-model="secondaryPhoneNumber"
				></v-text-field>
			</v-col>
			<v-col class="d-flex" cols="4" sm="2">			
				<v-select
					v-model="secondaryPhoneNumberType"
					:items="phonetype"
					label="Phone Type"
				></v-select>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="6" sm="6" md="3">
				<v-checkbox
					v-model="checkbox"
					label="I have no phone number"
				></v-checkbox>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="12" sm="12" md="6">
				<v-text-field
					required
					:rules="emailRules"
					label="Primary E-mail Address"
					v-model="primaryEmail"
				></v-text-field>
			</v-col>
			<v-col cols="12" sm="12" md="6">
				<v-text-field
					:rules="emailRules"
					label="Secondary E-mail Address"
					v-model="secondaryEmail"
				></v-text-field>
			</v-col>
		</v-row>
			<v-col cols="6" sm="6" md="3">
				<v-checkbox
					v-model="checkbox"
					label="I have no email address"
				></v-checkbox>
			</v-col>
		<v-row>
			<v-radio-group
				required
				:rules="[v => !!v || 'This field is required']"
				v-model="approval"
				label="May we contact you regarding follow up vaccination information?"
			>
				<v-col align="right" cols="3" sm="3" md="3">
					<v-radio label="Yes" value="yes"></v-radio>
					<v-radio label="No" value="no"></v-radio>
				</v-col>
			</v-radio-group>
		</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

  export default {
    name: 'SinglePatientContactInfo',
	data () {
		return { 
			phonetype:['Cell', 'Landline', 'Other'],
			radios: 'May we contact you for a follow up vaccination?',
			emailRules: [
				(v) =>
				/^[\s]*$|.+@.+\..+/.test(v) ||
				"E-mail must be in the format example@example.com",
			],
			primaryPhoneNumber: '',
			primaryPhoneNumberType: '',
			primaryEmail: '',
			secondaryPhoneNumber: '',
			secondaryPhoneNumberType: '',
			secondaryEmail: '',
			approval: ''
		}
	},
	methods: {
		sendHouseholdContactInfoInfoToReviewPage()
		{
			const householdContactInfoPayload = {
				primaryPhoneNumber: this.primaryPhoneNumber,
				primaryPhoneNumberType: this.primaryPhoneNumberType,
				primaryEmail: this.primaryEmail,
				secondaryPhoneNumber: this.secondaryPhoneNumber,
				secondaryPhoneNumberType: this.secondaryPhoneNumberType,
				secondaryEmail: this.secondaryEmail,
				approval: this.approval
			}
			EventBus.$emit('DATA_HOUSEHOLD_CONTACT_INFO_PUBLISHED', householdContactInfoPayload)
		},
		verifyFormContents()
		{
			var valid = true
			var message = "Woops! You need to enter the following fields:"
		
			if(this.primaryPhoneNumber == "") {
				message += "*Primary Phone Number"
				valid = false
			}
			if(this.primaryEmail == "") {
				message += "*Primary E-mail"
				valid = false
			}
			if(this.approval == "") {
				message += "*Follow up consent"
				valid = false
			}
			if (valid == false) {
				alert (message)
				return false
			}
		
			this.sendHouseholdContactInfoInfoToReviewPage();
			return true;
		}
	},
  }
</script>
