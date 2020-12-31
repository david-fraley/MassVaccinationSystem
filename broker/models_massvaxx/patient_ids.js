const Sequelize = require('sequelize');
module.exports = function(sequelize, DataTypes) {
  return sequelize.define('patient_ids', {
    patient_id: {
      autoIncrement: true,
      type: DataTypes.BIGINT,
      allowNull: false,
      primaryKey: true
    },
    assigner: {
      type: DataTypes.STRING(256),
      allowNull: false
    }
  }, {
    sequelize,
    tableName: 'patient_ids',
    schema: 'massvaxx',
    timestamps: false,
    indexes: [
      {
        name: "patient_ids_pkey",
        unique: true,
        fields: [
          { name: "patient_id" },
        ]
      },
    ]
  });
};
