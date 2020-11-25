<template>
  <v-container>
    <v-row>
      <v-col cols="6">
		<v-text-field
			placeholder="Complete"
			label="Vaccination Status" 
			id="vaccinationStatus" 
			v-model="vaccinationStatus"
			readonly
			filled
			prepend-icon="mdi-menu-right"
		></v-text-field></v-col>
      <v-col cols="6">
		<v-text-field
			placeholder="MM/DD/YYYY   HH/MM/SS"
			label="Completion Time" 
			id="completionTime" 
			v-model="completionTime"
			readonly
			filled
		></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp" 
			v-model="adverseReactionTimeStamp"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes" 
			v-model="notes"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
	<v-row>
	<v-col cols="12">
		<v-textarea
		id="moreReactions"
		placeholder="If more reactions, please list here"
		outlined
		rows="3"
		></v-textarea>
		</v-col>
    </v-row>
	
    <v-row align="center" justify="center">
      <v-col cols="6">
        <v-btn block color="accent">
          Submit observation record
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'AdverseEventsComponent',
    methods: 
    {
    },
    components: 
    {
    },
    data () {
		return {
		reactionList: [
			"Rash",
			"Trouble breathing",
			"Fever",
			"Fainting",
			],
      }
    }
  }
</script>
