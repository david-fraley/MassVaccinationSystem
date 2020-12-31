// Open a connection to the configured database using Sequelize.
// Ref: https://www.robinwieruch.de/postgres-express-setup-
// Ref: https://bezkoder.com/sequelize-associate-one-to-many/
//
const dbConfigs = require('../config/database');
const Sequelize = require('sequelize');

const sequelize = new Sequelize(dbConfigs.dbName, dbConfigs.username, dbConfigs.password, {
  dialect: dbConfigs.dialect,
  host: dbConfigs.host,
  logging: false
});

const db = {};

db.Sequelize = Sequelize;
db.sequelize = sequelize;

db.patient_ids = require('../models_massvaxx/patient_ids.js')(sequelize, Sequelize);
db.qr_codes = require('../models_massvaxx/qr_codes.js')(sequelize, Sequelize);

// +TODO+ Add in 1:N relationship
//db.patient_ids.hasMany(db.qr_codes, { as: 'qr_codes'});
//db.qr_codes.belongsTo(db.patient_ids, {
//    foreignKey: 'patient_id',
//    as: 'patient_ids'
//});

module.exports = db;