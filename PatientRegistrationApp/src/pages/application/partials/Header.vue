<template>
  <v-toolbar flat height="80em" color="primary" dark>
    <!--font size for xs & sm screens-->
    <div class="d-sm-none">
      <v-toolbar-title
        style="font-size:1.4rem"
        class="font-weight-bold text-wrap"
        >{{ title }}</v-toolbar-title
      >
    </div>
    <!--font size for screens larger than sm-->
    <div class="d-none d-sm-block">
      <v-toolbar-title
        style="font-size:2rem"
        class="font-weight-bold text-wrap"
        >{{ title }}</v-toolbar-title
      >
    </div>

    <v-spacer></v-spacer>
    <v-img max-height="5em" max-width="5em" src="@/assets/Logo.png"></v-img>
  </v-toolbar>
</template>
<script>
export default {
  name: "Header",
  data() {
    return {
      title: "COVID-19 Vaccination Registration",
    };
  },
};
</script>
<style lang="css" scoped>
.v-toolbar__title {
  font-size: 1.3rem;
}
</style>
