import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import VueMask from 'v-mask';
import htmlToPdf from 'html2canvas';

Vue.use(VueMask);
Vue.use(htmlToPdf);
Vue.config.productionTip = false

new Vue({
  vuetify,
  render: h => h(App)
}).$mount('#app')
