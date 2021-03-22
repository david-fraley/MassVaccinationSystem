<template>
  <v-app id="Patient-registration">
    <v-main>
      <SystemBar />
      <v-container fill-height fluid>
        <v-layout>
          <v-row>
            <v-col cols="12">
              <Header />
              <router-view />
            </v-col>
          </v-row>
        </v-layout>
      </v-container>
    </v-main>
    <v-footer app fixed>
      <span class="font-weight-thin">Version {{ version }}</span>
    </v-footer>
  </v-app>
</template>

<script>
import Header from "@/pages/application/partials/Header";

export default {
  name: "App",
  components: {
    Header,
    SystemBar,
  },
  computed: {
    version() {
      return process.env.VUE_APP_VERSION;
    },
  },
};
</script>
<style lang="css" scoped></style>
