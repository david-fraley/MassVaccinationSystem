<template>
  <v-container>
    <v-row>
      <v-col cols="6">
        <v-form ref="loginForm" v-model="formValid">
          <v-text-field
            v-model="user"
            :rules="[(v) => !!v || 'Username is required']"
            label="Username"
            required
          ></v-text-field>
          <v-text-field
            v-model="pass"
            :append-icon="!showPass ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[(v) => !!v || 'Password is required']"
            :type="showPass ? 'text' : 'password'"
            label="Password"
            @click:append="showPass = !showPass"
          ></v-text-field>
          <v-btn x-large block color="primary" @click="login">
            Login
          </v-btn>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "UserLoginPage",
  methods: {
    login() {
      this.formValid = this.$refs.loginForm.validate();
      if (this.formValid) {
        if (
          this.user.toLowerCase() === process.env.VUE_APP_USER.toLowerCase() &&
          this.pass.toLowerCase() === process.env.VUE_APP_PASS.toLowerCase()
        ) {
          this.$store.dispatch("loginUser", this.user);
          if (this.$router.currentRoute.query.next) {
            this.$router.push({
              path: this.$router.currentRoute.query.next,
            });
          } else {
            this.$router.push({
              path: "/",
            });
          }
        }
      }
    },
  },
  components: {},
  data() {
    return {
      user: "",
      pass: "",
      formValid: false,
      showPass: false,
    };
  },
};
</script>
