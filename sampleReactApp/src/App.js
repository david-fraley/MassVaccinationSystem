import React from 'react'
import { Route, Switch } from 'react-router-dom'
import Login from './login/login'
import Greeting from './registration/greetingPage/greeting'
import IndvForm from './registration/individualForm/indvForm'
import HouseForm from './registration/houseForm/houseForm'


class App extends React.Component {
  render() {
    return (
      <div className="App">
        <Switch>
          <Route path="/" component={Greeting} exact />
          <Route path="/individual" component={IndvForm} />
          <Route path="/household" component={HouseForm} />
          <Route component={Error} />
        </Switch>
        
      </div>
    )
  }
}

export default App