const config = require('../config.json');
const { Sequelize, DataTypes } = require('sequelize');

module.exports = db = {};

initialize();

async function initialize() {
    // create db if it doesn't already exist
    const { server, user, password, database, dialect } = config.database;

    // connect to db
    const sequelize = new Sequelize(database, user, password, {
        host: server,
        dialect: dialect,
        port: 3306,
        pool: {
            max: 10,
            min: 0,
            idle: 30000
        },
        logging: false
    });

    // init models and add them to the exported db object
    db.User = require('../models/user.model')(sequelize);
    db.Relationship = require('../models/relationship.model')(sequelize);

    // sync all models with database
    await sequelize.sync();
}