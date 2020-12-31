// Open a connection to the configured database using Sequelize.
// Ref: https://www.robinwieruch.de/postgres-express-setup-tutorial
//
const { Sequelize } = require('sequelize');
const configs = require('../config/database');
const initModels = require('../models_massvaxx/init-models.js');

const sequelize = new Sequelize(configs.dbName, configs.username, configs.password, {
  host: configs.host,
  dialect: configs.dialect
});

/*
const models = {
    PatientId: sequelize.import('../models_massvaxx/patient_ids'),
};

Object.keys(models).forEach(key => {
    if ('associate' in models[key]) {
        models[key].associate(models);
    }
});
*/

module.exports = initModels(sequelize);