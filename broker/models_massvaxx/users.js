const Sequelize = require('sequelize');
module.exports = function(sequelize, DataTypes) {
  return sequelize.define('users', {
    user_id: {
      autoIncrement: true,
      type: DataTypes.BIGINT,
      allowNull: false,
      primaryKey: true
    },
    username: {
      type: DataTypes.STRING(256),
      allowNull: false,
      unique: "users_username_key"
    },
    password: {
      type: DataTypes.STRING(1024),
      allowNull: false
    },
    last_changed: {
      type: DataTypes.DATEONLY,
      allowNull: false,
      defaultValue: "1970-01-01"
    },
    role: {
      type: DataTypes.STRING(256),
      allowNull: false,
      defaultValue: "CLIENT"
    }
  }, {
    sequelize,
    tableName: 'users',
    schema: 'massvaxx',
    timestamps: false,
    indexes: [
      {
        name: "users_pkey",
        unique: true,
        fields: [
          { name: "user_id" },
        ]
      },
      {
        name: "users_username_key",
        unique: true,
        fields: [
          { name: "username" },
        ]
      },
    ]
  });
};
