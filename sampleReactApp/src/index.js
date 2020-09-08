import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import './index.css'
import App from './App'

// (1) Targets the HTML page 'root' Div to display the application in
// (2) The App component built in App.js and exported as App will be included here
// (3) React StrictMode will check the code enclosed for errors (Do I want this in the example code?)
ReactDOM.render(
    <React.StrictMode> {/*(3)*/}
        <BrowserRouter>
            <App /> {/* (2) */}
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root') // (1) 
);