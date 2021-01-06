var DataTypes = require("sequelize").DataTypes;
var _patient_ids = require("./patient_ids");
var _qr_codes = require("./qr_codes");
var _users = require("./users");

function initModels(sequelize) {
  var patient_ids = _patient_ids(sequelize, DataTypes);
  var qr_codes = _qr_codes(sequelize, DataTypes);
  var users = _users(sequelize, DataTypes);

  qr_codes.belongsTo(patient_ids, { foreignKey: "patient_id"});
  patient_ids.hasMany(qr_codes, { foreignKey: "patient_id"});

  return {
    patient_ids,
    qr_codes,
    users,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
