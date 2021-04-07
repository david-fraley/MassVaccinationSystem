import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import VueMask from 'v-mask';
import store from '@/store/store';
import VueRouter from 'vue-router';
import router from '@/router/router';

Vue.use(VueMask);
Vue.use(VueRouter);
Vue.config.productionTip = false;

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
