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
			v-model="reactionList_1"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_1" 
			v-model="adverseReactionTimeStamp_1"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_1" 
			v-model="notes_1"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_2"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_2" 
			v-model="adverseReactionTimeStamp_2"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_2" 
			v-model="notes_2"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_3"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_3" 
			v-model="adverseReactionTimeStamp_3"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_3" 
			v-model="notes_3"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_4"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_4" 
			v-model="adverseReactionTimeStamp_4"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_4" 
			v-model="notes_4"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_5"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_5" 
			v-model="adverseReactionTimeStamp_5"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_5" 
			v-model="notes_5"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_6"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_6" 
			v-model="adverseReactionTimeStamp_6"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_6" 
			v-model="notes_6"
			placeholder="If other, list reaction"
        ></v-text-field></v-col>
    </v-row>
	
		<v-row>
		<v-col cols="4">
		<v-select
			v-model="reactionList_7"
			:items="reactionList"
			label="Reaction List"
			prepend-icon="mdi-menu-right"
        ></v-select></v-col>

		<v-col cols="4">
        <v-text-field 
			id="adverseReactionTimeStamp_7" 
			v-model="adverseReactionTimeStamp_7"
			placeholder="MM/DD/YYYY HH/MM/SS"
        ></v-text-field></v-col>
		
		<v-col cols="4">
        <v-text-field 
			id="notes_7" 
			v-model="notes_7"
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
