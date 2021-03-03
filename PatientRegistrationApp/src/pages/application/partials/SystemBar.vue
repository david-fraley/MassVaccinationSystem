<template>
  <v-system-bar
    dark
    color="red"
    height="40 rem">
    <v-icon color="white" large>mdi-alert-circle-outline</v-icon>
    <span class="white--text"><strong>TEST WEBSITE - NOT LIVE</strong></span>
  </v-system-bar>
</template>
<script>
export default {
  name: "SystemBar",
  data() {
    return {
    };
  },
};
</script>
<style lang="css" scoped>
.v-system-bar {
  font-size: 2rem;
}
</style>
