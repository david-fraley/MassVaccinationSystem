<template>
<v-form ref="form" v-model="valid">
	<v-container fluid>
		<v-row align="center" justify="start">
			<v-col cols="12">
				<div>{{disclosureStatement}}</div>  
				<div><br>{{consequenceStatement}}</div>
				<div><strong>We strongly encourage you to provide your e-mail and/or phone number as contact information.</strong></div>
				<div><br></div>
				<v-divider></v-divider>
				<div><br>By providing your e-mail and/or phone number as contact information, you agree to the following:</div>
				<div><strong>{{acknowledgementStatement}}</strong></div>
			</v-col>
		</v-row> 
		<v-row align="center" justify="start" no-gutters>
			<v-checkbox
				v-model="acknowledgementCheckBox">
				<template #label>
				<span class="red--text"><strong>* <br></strong></span>I have read and understood the above.
				</template>
			</v-checkbox>
		</v-row>
		<v-row align="center" justify="start" no-gutters>
			<v-checkbox
				v-model="permissionCheckBox"
				:disabled="!acknowledgementCheckBox"
				label="I agree to provide and authorize the use of my contact information as outlined above."
				>
			</v-checkbox>
		</v-row>
			<v-divider></v-divider>
		<v-row align="center" justify="start">
			<v-col cols="12" sm="6" md="6" lg="3">
				<v-text-field
					v-model="primaryPhoneNumber"
					:disabled="!permissionCheckBox"
					:rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
					v-mask="'(###)###-####'"
					prepend-icon="mdi-phone"
					label="Primary Phone Number">
				</v-text-field>
			</v-col>
			<v-col cols="11" sm="5" md="5" lg="2">
				<v-select
					v-model="primaryPhoneNumberType"
					:disabled="!permissionCheckBox"
					:items="phoneTypeOptions"
					label="Phone Type"
					prepend-icon="mdi-blank"
				></v-select>
			</v-col>
			<v-col cols="1" sm="1" md="1" lg="1">
			</v-col>
			<v-col cols="12" sm="6" md="6" lg="3">
				<v-text-field
					v-model="secondaryPhoneNumber"
					:disabled="!primaryPhoneNumber"
					:rules="[v => v.length === 13 || 'Phone number must be 10 digits']"
					v-mask="'(###)###-####'"
					label="Secondary Phone Number"
					prepend-icon="mdi-phone">
				</v-text-field>
			</v-col>
			<v-col cols="11" sm="5" md="5" lg="2">
				<v-select
					v-model="secondaryPhoneNumberType"
					:disabled="!primaryPhoneNumber"
					:items="phoneTypeOptions"
					label="Phone Type"
					prepend-icon="mdi-blank"
				></v-select>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="11" sm="5" md="5" lg="5">
				<v-text-field
					v-model="primaryEmail"
					:disabled="!permissionCheckBox"
					:rules="emailRules"
					prepend-icon="mdi-email"
					label="Primary E-mail Address">
				</v-text-field>
			</v-col>
			<v-col cols="1" sm="1" md="1" lg="1">
			</v-col>
			<v-col cols="11" sm="5" md="5" lg="5">
				<v-text-field
					v-model="secondaryEmail"
					:disabled="!primaryEmail"
					:rules="emailRules"
					prepend-icon="mdi-email"
					label="Secondary E-mail Address">
				</v-text-field>
			</v-col>
		</v-row>
	</v-container>
</v-form>
</template>

<script>
import EventBus from '../eventBus'
import customerSettings from '../customerSettings'

  export default {
    name: 'SinglePatientContactInfo',
	data () {
		return { 
			phoneTypeOptions:['Home', 'Mobile', 'Work'],
			emailRules: [
				(v) =>
				/^[\s]*$|.+@.+\..+/.test(v) ||
				"Please provide a valid e-mail address",
			],
			primaryPhoneNumber: '',
			primaryPhoneNumberType: '',
			primaryEmail: '',
			secondaryPhoneNumber: '',
			secondaryPhoneNumberType: '',
			secondaryEmail: '',
			acknowledgementCheckBox: false,
			permissionCheckBox: false,
			disclosureStatement: customerSettings.contactInfoDisclosure,
			consequenceStatement: customerSettings.contactInfoConsequence,
			acknowledgementStatement: customerSettings.contactInfoAcknowledgement,
			valid: false,
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
			}
			EventBus.$emit('DATA_HOUSEHOLD_CONTACT_INFO_PUBLISHED', householdContactInfoPayload)
		},
		verifyFormContents()
		{
			var valid = true
			var message 

			if(this.acknowledgementCheckBox)
			{	
				if(this.permissionCheckBox)
				{
					if((this.primaryPhoneNumber == "") && (this.primaryEmail == ""))
					{
						message = "Please provide an e-mail address and/or phone number."
						valid = false
					}
				}
			}
			else
			{
				message = "Acknowledgement is required."
				valid = false
			}
			
			if (valid == false) 
			{
				alert (message)
				return false
			}
				this.$refs.form.validate();
				if (!this.valid) return;	
			this.sendHouseholdContactInfoInfoToReviewPage();
			return true;
		},
	},
}
</script>

