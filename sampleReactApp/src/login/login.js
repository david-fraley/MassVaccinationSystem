import React from 'react'
import '../generalStyles.css'
import './login.css'

class Login extends React.Component {
  render() {
    // return always 
    return (
      <div className="login flex-column flex-center">
        <h1>Log In Here!</h1>
        <form id="search-form" className="flex-column flex-center" onsubmit="loadTestData(event)">
        {/* Do not forget closing tags on things like input and img elements */}
            <input type="text" id="name" value="Name" />
            <input type="text" id="id" value="Id" />
            <input type="submit" id="submitCreds" value="Login" />
        </form> 
        <p>Not registered?</p>
        <p id="clickForRegister" className="link">Click here!</p>
      </div>
    )
  }
}

export default Login

