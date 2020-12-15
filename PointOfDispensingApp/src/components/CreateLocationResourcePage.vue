<template>
  <v-container>
    <v-row>
      <h2 class="font-weight-medium primary--text">Location Resources</h2>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <div class="font-weight-medium secondary--text">Enter one or more search parameters to retrieve the available location resources.</div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">City</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="search_city"
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">Zip Code</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="search_postalCode"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
      <v-col cols="1">
        <!--blank column for spacing-->
      </v-col>
      <v-col cols="5">
        <v-row no-gutters>
          <v-col cols="4">
            <div class="secondary--text">State</div>
          </v-col>
          <v-col cols="8">
            <v-select
              :items="stateOptions"
              outlined
              dense
              v-model="search_state"
            ></v-select>
          </v-col>
          <v-col cols="4">
            <div class="secondary--text">Organization</div>
          </v-col>
          <v-col cols="8">
            <v-text-field outlined dense 
              v-model="search_organization"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row> 	
    <v-row no-gutters>
      <v-col cols="12">
        <v-btn color="accent" @click="retrieveLocationResources()">
          Search
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-divider></v-divider>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <template>
          <v-data-table
            @click:row="rowClick"
            item-key="siteID"
            single-select
            :items="locationResourceLookupTable"
            :headers="headers"
            class="elevation-1"
            :footer-props="{
              'items-per-page-options':[5]
            }"
            :items-per-page="5"
          ></v-data-table>
        </template>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-dialog
          v-model="createLocationResourceDialog"
          persistent
          max-width="70%"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="accent" v-bind="attrs" v-on="on">
                Create location resource
              </v-btn>
            </template>
            <v-card>
              <v-card-title class="headline grey lighten-2 justify-center">
                Create Location Resource
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">Site ID</div>
                    </v-col>
                    <v-col cols="9">
                      <v-text-field outlined dense 
                        v-model="identifier_value"
                        placeholder="(Unique identifier)"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">Organization</div>
                    </v-col>
                    <v-col cols="9">
                      <v-text-field outlined dense 
                        v-model="identifier_assigner"
                        placeholder="(Organization responsible for this location)"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">Site Name</div>
                    </v-col>
                    <v-col cols="9">
                      <v-text-field outlined dense 
                        v-model="identifier_assigner"
                        placeholder="(Name of the location)"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="12">
                      <v-divider></v-divider>
                      <div><br></div>
                    </v-col>
                  </v-row>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">Street Address</div>
                    </v-col>
                    <v-col cols="9">
                      <v-text-field outlined dense 
                        v-model="address_line"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">City</div>
                    </v-col>
                    <v-col cols="4">
                      <v-text-field outlined dense 
                        v-model="address_city"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="1">
                      <!-- blank column for spacing -->
                    </v-col>
                    <v-col cols="2">
                      <div class="secondary--text">State</div>
                    </v-col>
                    <v-col cols="2">
                      <v-select
                        :items="stateOptions"
                        outlined
                        dense
                        v-model="address_state"
                      ></v-select>
                    </v-col>
                  </v-row>
                  <v-row no-gutters>
                    <v-col cols="2">
                      <div class="secondary--text">Zip Code</div>
                    </v-col>
                    <v-col cols="4">
                      <v-text-field outlined dense 
                        v-model="address_postalCode"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="1">
                      <!-- blank column for spacing -->
                    </v-col>
                    <v-col cols="2">
                      <div class="secondary--text">Country</div>
                    </v-col>
                    <v-col cols="2">
                      <v-select
                        :items="countryOptions"
                        outlined
                        dense
                        v-model="address_country"
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-btn
                  color="primary"
                  text
                  left
                  @click="createLocationResourceDialog = false"
                >
                  Cancel
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn
                  color="primary"
                  text
                  right
                  @click="submitLocationResource()"
                >
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
      </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'CreateLocationResourcePage',
    methods: 
    {
      submitLocationResource() {
        this.createLocationResourceDialog = false
        //do nothing yet
      },
      retrieveLocationResources() {
        //send search parameters
        //populate
      }
    },
    components: 
    {
    },
    data () {
      return {
        stateOptions:[
        'AL', 'AK', 'AS', 'AZ',
        'AR', 'CA', 'CO', 'CT',
        'DE', 'DC', 'FM',
        'FL', 'GA', 'GU', 'HI', 'ID',
        'IL', 'IN', 'IA', 'KS', 'KY',
        'LA', 'ME', 'MH', 'MD',
        'MA', 'MI', 'MN', 'MS',
        'MO', 'MT', 'NE', 'NV',
        'NH', 'NJ', 'NM', 'NY',
        'NC', 'ND', 'MP', 'OH',
        'OK', 'OR', 'PW', 'PA', 'PR',
        'RI', 'SC', 'SD', 'TN',
        'TX', 'UT', 'VT', 'VI', 'VA',
        'WA', 'WV', 'WI', 'WY',
        ],
        countryOptions: ['USA'],
        search_state: 'WI',
        address_state: 'WI',
        address_country: 'USA',
        headers: [
          {
            text: 'Organization',
            align: 'start',
            sortable: false,
            value: 'organizationName',
          },
          { text: 'Site Name', value: 'siteName' },
          { text: 'Site ID', value: 'siteID' },
          { text: 'Address', value: 'siteAddress' },
          { text: 'City', value: 'siteCity' },
        ],
        createLocationResourceDialog: false,
        locationResourceLookupTable: [
          {
            siteID: '123456',
            organizationName: 'Waukesha County',
            siteName: 'Fire Station XYZ',
            siteAddress: '6585 Lake St',
            siteCity: 'Glendale',
          },
        ]
      }
    }
  }
</script>
