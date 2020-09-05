<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <button @click="sendRequest" class="btn btn-outline-success my-4">Send Request ></button>
    <p class="lead">{{ response }}</p>
  </div>
</template>

<script>
import axios from 'axios'
const client = axios.create({
  baseURL: 'http://localhost:3000',
  json: true
})
export default {
  name: 'HelloWorld',
  data: function() {
    return {
      response: 'No data yet...'
    }
  },
  props: {
    msg: String
  },
  methods: {
    sendRequest() {
      client({
        method: 'get',
        url: '/'
      }).then((res) => {
        this.response = res.data.message
      }).catch((error) => {
        this.response = error
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>