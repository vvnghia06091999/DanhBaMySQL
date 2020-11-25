const { DataTypes } = require('sequelize');

module.exports = model;

function model(sequelize) {
    const attributes = {
        // phone: { type: DataTypes.STRING },
        // email: { type: DataTypes.STRING },
        // password: { type: DataTypes.STRING},
        // fullName: { type: DataTypes.STRING},
        // gender: { type: DataTypes.STRING}
        phone: { type: DataTypes.STRING },
        email: { type: DataTypes.STRING },
        photoURL: { type: DataTypes.STRING },
        fullName: { type: DataTypes.STRING, allowNull: false },
        gender: { type: DataTypes.BOOLEAN, allowNull: false , defaultValue: false},
        dob: { type: DataTypes.DATEONLY, allowNull: false , defaultValue: DataTypes.NOW },
        disabled: { type: DataTypes.BOOLEAN, allowNull: false , defaultValue: false }
    };

    return sequelize.define('User', attributes);
}