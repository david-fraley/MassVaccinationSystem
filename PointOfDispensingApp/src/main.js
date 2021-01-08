import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import VueRouter from 'vue-router'
import navRoutes from './navRoutes.js'
import store from './store/store'

Vue.config.productionTip = false
Vue.use(VueRouter)

const router = new VueRouter({
  routes: navRoutes
});

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
