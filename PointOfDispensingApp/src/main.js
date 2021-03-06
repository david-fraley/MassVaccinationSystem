import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import VueRouter from 'vue-router'
import router from './router/router.js'
import store from './store/store'
import VueMask from 'v-mask';

Vue.use(VueMask);

Vue.config.productionTip = false;
Vue.use(VueRouter);

new Vue({
  vuetify,
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
