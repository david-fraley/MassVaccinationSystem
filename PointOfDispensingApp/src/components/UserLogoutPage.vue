<template>
  <h1>Logging out...</h1>
</template>
<script>

export default {
  name: 'Logout',
  mounted() {
    this.$store.dispatch("logoutUser");
    this.$router.push({
      path: "/UserLogin",
    });
  },
};
</script>