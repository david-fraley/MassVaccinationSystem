import Vue from 'vue';
import Vuetify from 'vuetify/lib';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
          light: {
            primary: '#9E9E9E',
            secondary: '#424242',
            accent: '#9E9E9E',
            pageColor: '#FFFFFF',
            appTitleColor: '#009688'
          }
        }
      },
});
