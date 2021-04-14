// Read private configuration settings from .env file into process.env
//
require("dotenv").config({ path: `${__dirname}/../../.env` });
const session = require('express-session');
const keycloak = require('keycloak-connect');

let memoryStore = new session.MemoryStore();
let keycloakConfig = {
  clientId: process.env.KEYCLOAK_CLIENT_ID || 'massvaxx',
  bearerOnly: true,
  serverUrl: process.env.KEYCLOAK_SERVER_URL || 'https://massvaxx-keycloak.mooo.com/auth/',
  realm: process.env.KEYCLOAK_REALM || 'MassVaxx'
};

module.exports.session = new session({
  secret: process.env.SESSION_SERVER_SECRET || 'notVerySecret',
  resave: false,
  saveUninitialized: true,
  store: module.exports.memoryStore
});

module.exports.keycloak = new keycloak({
  store: memoryStore
}, keycloakConfig);