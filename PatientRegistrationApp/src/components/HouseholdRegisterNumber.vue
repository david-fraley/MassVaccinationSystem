<template>
	<v-container fluid>
		<v-row align="center" justify="center">
			<v-col class="d-flex" cols="8">
				<v-select
					v-model="numberOfPatients"
					required
					:items="[2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]"
					prepend-icon="mdi-account-group">
						<template #label>
						<span class="red--text"><strong>* </strong></span>How many people will you be registering (including yourself)?
						</template>
				</v-select>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import EventBus from '../eventBus'

export default {
data () {
	return {
		numberOfPatients: '',
		e1: 1,
		rules: [
			v => v <= 20 || 'Maximum of 20 people',
			v => v >= 2 || 'Minimum of 2 people' ]
	}
},
watch: {
	numberOfPatients (val) {
		if (this.e1 != val) {
			this.e1 = val
			const householdCountPayload = {
				householdCount: this.e1
			}
			EventBus.$emit('DATA_HOUSEHOLD_COUNT_UPDATED', householdCountPayload)
		}
	},
},
methods: {
	verifyFormContents() {
		let valid = true
		if(this.numberOfPatients == '')
		{
			alert('Please enter the number of people to be registered.')
			valid = false
		}
		return valid
	},
}
}
</script>

<style lang="css" scoped>
.required:before{
	content:"*";
	color: red;
	font-weight:bold;
}
</style>

