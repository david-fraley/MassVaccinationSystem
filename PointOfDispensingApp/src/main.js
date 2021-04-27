import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import VueRouter from 'vue-router'
import router from './router/router.js'
import store from './store/store'
import VueMask from 'v-mask';
import VueLogger from 'vuejs-logger';
import Keycloak from 'keycloak-js';
import configKeycloak from './KeycloakConfig.js';

Vue.use(VueMask);

Vue.config.productionTip = false;
Vue.use(VueRouter);

Vue.use(VueLogger);

let keycloak = Keycloak(configKeycloak);

keycloak.init({ onLoad: configKeycloak.onLoad, checkLoginIframe: false }).then((auth) => {
  if(!auth) {
    window.location.reload();
  } else {
    Vue.$log.info("Authenticated");
    
    store.state.keycloak= keycloak;

    localStorage.setItem("loggedIn", auth)
    store.state.currentUser.loggedIn=localStorage.getItem("loggedIn")
    
    localStorage.setItem("username", keycloak.tokenParsed.preferred_username)
    store.state.currentUser.name=localStorage.getItem("username")

    localStorage.setItem("exp", keycloak.tokenParsed.exp);
    store.state.currentUser.exp=localStorage.getItem("exp")

    new Vue({
      vuetify,
      router,
      store,
      render: (h) => h(App),
    }).$mount("#app");

  }

  setInterval(() => {
    keycloak.updateToken(70).then((refreshed) => {
      if (refreshed) {
        Vue.$log.info('Token refreshed');
      } else {
        Vue.$log.warn('Token not refreshed, valid for '
          + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
      }
    }).catch(() => {
      Vue.$log.error('Failed to refresh token');
    });
  }, 6000)
  

}).catch(() => {
  Vue.$log.error("Authenticated Failed");
});
