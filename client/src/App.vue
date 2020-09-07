<!-- Commented out our big front end to specifically show auth demo
<template>
  <div id="app">
    <div id="davidapp">
      <div class="header">
        <h1>Firebase Authentication Demo</h1>
        <p>Integrated a front/back end with firebase to prove authentication works and plot out how to use it</p>
      </div>

      <div class="topnav">
        <a href="#">Link</a>
        <a href="#">Link</a>
        <a href="#">Link</a>
        <a href="#" style="float:right">Link</a>
      </div>
      <div class="row">
        <div class="leftcolumn">
          <div class="card">
            <h2>TITLE HEADING</h2>
            <h5>Title description, Dec 7, 2017</h5>
            <div class="fakeimg" style="height:200px;">Image</div>
            <p>Some text..</p>
            <p>Sunt in culpa qui officia deserunt mollit anim id est laborum consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco.</p>
          </div>
          <div class="card">
            <h2>TITLE HEADING</h2>
            <h5>Title description, Sep 2, 2017</h5>
            <div class="fakeimg" style="height:200px;">Image</div>
            <p>Some text..</p>
            <p>Sunt in culpa qui officia deserunt mollit anim id est laborum consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco.</p>
          </div>
        </div>
        <div class="rightcolumn">
          <div class="card">
            <h2>About Me</h2>
            <div class="fakeimg" style="height:100px;">Image</div>
            <p>Some text about me in culpa qui officia deserunt mollit anim..</p>
          </div>
          <div class="card">
            <h3>Popular Post</h3>
            <div class="fakeimg"><p>Image</p></div>
            <div class="fakeimg"><p>Image</p></div>
            <div class="fakeimg"><p>Image</p></div>
          </div>
          <div class="card">
            <h3>Follow Me</h3>
            <p>Some text..</p>
          </div>
        </div>
      </div>

      <div class="footer">
        <h2>Footer</h2>
        <br>
        <div id="vueapp">
          <HelloWorld msg="Firebase Authentication Demo"/>
          <button @click="signIn" class="btn btn-outline-primary mx-4">Sign In ></button>
          <button @click="sendRequest" class="btn btn-outline-success mx-4">Send Request ></button>
          <button @click="signOut" class="btn btn-outline-danger mx-4">Sign Out ></button>
        </div>
      </div>
    </div>
  </div>
</template>
--> 


<template>
  <div class="hello">
      <h1>{{ msg }}</h1>
    <p class="lead mt-2">{{ authStatus }}</p>
    <div class="d-flex my-4 justify-content-center">
      <button @click="signIn" class="btn btn-outline-primary mx-4">Sign In ></button>
      <button @click="sendRequest" class="btn btn-outline-success mx-4">Send Request ></button>
      <button @click="signOut" class="btn btn-outline-danger mx-4">Sign Out ></button>
    </div>
    <p class="lead">{{ response }}</p>
  </div>
</template>


<script>
import axios from 'axios'
import firebase from 'firebase'

const client = axios.create({
  baseURL: 'http://localhost:3000',
  json: true
})

export default {
  name: 'HelloWorld',
  data: function() {
    return {
      response: 'No data yet...',
      authStatus: 'No Auth Status'
    }
  },
  props: {
    msg: String
  },
  methods: {
    sendRequest() {
      if (firebase.auth().currentUser) {
        firebase.auth().currentUser.getIdToken(true)
        .then((idToken) => {
          client({
            method: 'get',
            url: '/',
            headers: {
              'AuthToken': idToken
            }
          }).then((res) => {
            this.response = res.data.message
          }).catch((error) => {
            this.response = error
          })
        }).catch((error) => {
          this.response = "Error getting auth token"
        });
      } else {
        client({
          method: 'get',
          url: '/'
        }).then((res) => {
          this.response = res.data.message
        }).catch((error) => {
          this.response = error
        })
      }
    },
    signIn() {
      firebase.auth()
      .signInWithEmailAndPassword("dummy@gmail.com", "pass123!")
      .then(() => {
        this.authStatus = 'Authorized'
      }).catch((err) => {
        this.authStatus = err
      })
    },
    signOut() {
      firebase.auth().signOut().then(() => {
        this.authStatus = 'Unauthorized'
      }).catch((err) => {
        this.authStatus = err
      })
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
* {
  box-sizing: border-box;
}

body {
  font-family: Arial;
  padding: 10px;
  background: #f1f1f1;
}

/* Header/Blog Title */
.header {
  padding: 30px;
  text-align: center;
  background: white;
}

.header h1 {
  font-size: 50px;
}

/* Style the top navigation bar */
.topnav {
  overflow: hidden;
  background-color: #333;
}

/* Style the topnav links */
.topnav a {
  float: left;
  display: block;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}

/* Change color on hover */
.topnav a:hover {
  background-color: #ddd;
  color: black;
}

/* Create two unequal columns that floats next to each other */
/* Left column */
.leftcolumn {   
  float: left;
  width: 75%;
}

/* Right column */
.rightcolumn {
  float: left;
  width: 25%;
  background-color: #f1f1f1;
  padding-left: 20px;
}

/* Fake image */
.fakeimg {
  background-color: #aaa;
  width: 100%;
  padding: 20px;
}

/* Add a card effect for articles */
.card {
  background-color: white;
  padding: 20px;
  margin-top: 20px;
}

/* Clear floats after the columns */
.row:after {
  content: "";
  display: table;
  clear: both;
}

/* Footer */
.footer {
  padding: 20px;
  text-align: center;
  background: #ddd;
  margin-top: 20px;
}

/* Responsive layout - when the screen is less than 800px wide, make the two columns stack on top of each other instead of next to each other */
@media screen and (max-width: 800px) {
  .leftcolumn, .rightcolumn {   
    width: 100%;
    padding: 0;
  }
}

/* Responsive layout - when the screen is less than 400px wide, make the navigation links stack on top of each other instead of next to each other */
@media screen and (max-width: 400px) {
  .topnav a {
    float: none;
    width: 100%;
  }
}
</style>
