import Vue from 'vue'
import App from './App.vue'
import firebase from 'firebase'

firebase.initializeApp(
  {
    apiKey: "AIzaSyDnAI9imzUlYuIYEaPD7sNRxnhY7Yubh9c",
    authDomain: "awesome-fbauthdemo.firebaseapp.com",
    databaseURL: "https://awesome-fbauthdemo.firebaseio.com",
    projectId: "awesome-fbauthdemo",
    storageBucket: "awesome-fbauthdemo.appspot.com",
    messagingSenderId: "150158205673",
    appId: "1:150158205673:web:c9dac1dd3e87adf64fc678"
  }
)

firebase.auth().currentUser.getIdToken(true)
  .then((idToken) => {
    client({
    method: 'get',
    url: '/',
    headers: {
      'AuthToken': idToken
    }
  })
})

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
