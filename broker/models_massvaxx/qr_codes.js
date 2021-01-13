const Sequelize = require('sequelize');
module.exports = function(sequelize, DataTypes) {
  return sequelize.define('qr_codes', {
    qr_code_id: {
      autoIncrement: true,
      type: DataTypes.BIGINT,
      allowNull: false,
      primaryKey: true
    },
    patient_id: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'patient_ids',
        key: 'patient_id'
      }
    },
    qr_code: {
      type: DataTypes.STRING(256),
      allowNull: true,
      unique: "qr_codes_qr_code_key"
    }
  }, {
    sequelize,
    tableName: 'qr_codes',
    schema: 'massvaxx',
    timestamps: false,
    indexes: [
      {
        name: "qr_codes_pkey",
        unique: true,
        fields: [
          { name: "qr_code_id" },
        ]
      },
      {
        name: "qr_codes_qr_code_key",
        unique: true,
        fields: [
          { name: "qr_code" },
        ]
      },
    ]
  });
};
