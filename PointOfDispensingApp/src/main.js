import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import VueRouter from 'vue-router'
import navRoutes from './router/navRoutes.js'
import store from './store/store'
import VueMask from 'v-mask';
import VueLogger from 'vuejs-logger';
import Keycloak from 'keycloak-js';
import configKeycloak from './KeycloakConfig.js';

Vue.use(VueMask);

Vue.config.productionTip = false;
Vue.use(VueRouter);

const router = new VueRouter({
  routes: navRoutes,
});

Vue.use(VueLogger);

let keycloak = Keycloak(configKeycloak);

keycloak.init({ onLoad: configKeycloak.onLoad, checkLoginIframe: false }).then((auth) => {
  if(!auth) {
    window.location.reload();
  } else {
    Vue.$log.info("Authenticated");
    store.state.keycloak= keycloak;
    new Vue({
      vuetify,
      router,
      store,
      render: (h) => h(App),
    }).$mount("#app");

    localStorage.setItem("loggedIn", true)
    store.state.currentUser.loggedIn=localStorage.getItem("loggedIn")
    
    localStorage.setItem("username", keycloak.username)
    store.state.currentUser.name=localStorage.getItem("username")

    let exp=  Date.now() + (1000*60*60*process.env.VUE_APP_EXP_LOGIN_HOURS); // add 24 hours of millis

    localStorage.setItem("exp", exp)
    store.state.currentUser.exp=localStorage.getItem("exp")
  }

  // setInterval(() => {
  //   keycloak.updateToken(70).then((refreshed) => {
  //     if (refreshed) {
  //       Vue.$log.info('Token refreshed' + refreshed);
  //     } else {
  //       Vue.$log.warn('Token not refreshed, valid for '
  //         + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
  //     }
  //   }).catch(() => {
  //     Vue.$log.error('Failed to refresh token');
  //   });
  // }, 6000)
  

}).catch(() => {
  Vue.$log.error("Authenticated Failed");
});
