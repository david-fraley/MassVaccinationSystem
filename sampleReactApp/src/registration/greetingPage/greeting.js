import React from 'react'
import { Link } from 'react-router-dom'
import '../../generalStyles.css'
import './greeting.css'
import Household from './regHousehold'
import Myself from './regMyself'

class Greeting extends React.Component {
    render() {
      // return always 
      return (
        <div className="greeting flex-column flex-center center-vertical">
          <h1>COVID-19 Vaccine Registration</h1>
          <div id="div-single-household" className="flex-row flex-center">
              <Household> </Household>
              <img id="seperator" href='../media/separator.png'
               alt="image separating choices"  />
              <Myself> </Myself>
          </div> 
          <div id="continue">
              <p id="test" class="float-right" onClick={renderForm}> Next </p>
              <Link to="/individual">Render Next Page </Link>
          </div>
        </div>
      )
    }
}
  
export default Greeting
  
function renderForm(ev) {
    let testVal = document.querySelector("#test");
    testVal.innerHTML = "Back";
}