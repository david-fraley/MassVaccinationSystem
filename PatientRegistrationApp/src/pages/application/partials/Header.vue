<template>
  <v-toolbar flat height="80em" color="primary" dark>
    <div>
      <v-toolbar-title
        class="font-weight-bold text-wrap"
        :class="[$vuetify.breakpoint.xs?'font-xs':'font-sm']"
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
.font-sm {
  font-size: 2rem
}
.font-xs {
  font-size: 1rem
}
</style>
