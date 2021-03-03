import Vue from "vue";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import VueMask from "v-mask";

Vue.use(VueMask);
Vue.config.productionTip = false;

new Vue({
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
