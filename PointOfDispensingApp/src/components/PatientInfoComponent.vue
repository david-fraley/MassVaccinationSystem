<template>
  <v-container>
    <v-row>
      <v-col cols="3">
        <v-img 
          max-height="220" 
          max-width="200"
          src="../assets/blankPicture.png">
        </v-img>
        <div class="font-weight-medium">Patient ID:  <span class="font-weight-regular">1234567890</span></div>
      </v-col>

      <v-col cols="9">
        <v-row no-gutters>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Last Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              value="Smith"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">First Name</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              value="John"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Date of Birth</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              value="01/01/1970"
            ></v-text-field>
          </v-col>
          <v-col cols="1">
            <!--space between columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Gender</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              value="Male"
            ></v-text-field>
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Preferred Language</div>
          </v-col>
          <v-col cols="3">
            <v-text-field filled dense readonly outlined 
              value="English"
            ></v-text-field>
          </v-col>
          <v-col cols="6">
            <!--empty columns-->
          </v-col>
          <v-col cols="2">
            <div class="font-weight-medium secondary--text">Address</div>
          </v-col>
          <v-col cols="9">
            <v-text-field filled dense readonly outlined 
              value="1234 Main Street, Milwaukee, WI 53233"
            ></v-text-field>
          </v-col>
        </v-row>
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
    name: 'PatientInfoComponent',
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
