// Read private configuration settings from .env file into process.env
//
require("dotenv").config({ path: `${__dirname}/../../.env` });

// Export configuration settings with some values set to defaults.
// NOTE: login/password settings should not be defaulted.
//
module.exports = {
    // For Sequelize (defaults below for PostgreSQL)
    //
    dbName: process.env.DB_NAME || 'massvaxx',
    dialect: process.env.DB_DIALECT || 'postgres',
    host: process.env.DB_HOST || 'localhost',
    password: process.env.DB_PASSWORD,
    port: process.env.DB_PORT || 5432,
    username: process.env.DB_USERNAME
}
