/**
 * Initializes and exports database connection (sequelize) and 
 * model wrappers for Broker DB.
 * 
 * @author Todd Jensen
 * @author www.massvaxx.com
 */

// Implementation references:
// [1] https://www.robinwieruch.de/postgres-express-setup-
// [2] https://bezkoder.com/sequelize-associate-one-to-many/
//
const dbConfigs = require('../config/database');
const Sequelize = require('sequelize');

const sequelize = new Sequelize(dbConfigs.dbName, dbConfigs.username, dbConfigs.password, {
  dialect: dbConfigs.dialect,
  host: dbConfigs.host,
  port: dbConfigs.port,
  logging: false
});

const db = {};

db.Sequelize = Sequelize;
db.sequelize = sequelize;

db.patient_ids = require('../models_massvaxx/patient_ids.js')(sequelize, Sequelize);
db.qr_codes = require('../models_massvaxx/qr_codes.js')(sequelize, Sequelize);

// Add in 1:N relationships
//
db.patient_ids.hasMany(db.qr_codes, { foreignKey: 'patient_id', as: 'qr_codes'});

module.exports = db;
