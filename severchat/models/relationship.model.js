const { DataTypes } = require('sequelize');

module.exports = model;

function model(sequelize) {
    const attributes = {
        idUser1: {type: DataTypes.INTEGER,allowNull: false,primaryKey: true, references: {
            model: 'Users',
            key: 'id'
          }},
        idUser2: {type: DataTypes.INTEGER,allowNull: false,primaryKey: true, references: {
            model: 'Users',
            key: 'id'
          }},
        idUserAction: {type: DataTypes.INTEGER,allowNull: false, references: {
            model: 'Users',
            key: 'id'
          }},
        status: {type: DataTypes.STRING,allowNull: false}
    };

    return sequelize.define('Relationship', attributes);
}