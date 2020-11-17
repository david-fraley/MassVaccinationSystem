import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import VueRouter from 'vue-router'
import navRoutes from './navRoutes.js';

Vue.config.productionTip = false
Vue.use(VueRouter)

const router = new VueRouter({
  routes: navRoutes
});

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
