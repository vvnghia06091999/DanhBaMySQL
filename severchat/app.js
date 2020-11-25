const express = require("express");
const cookieParser = require('cookie-parser');
const config = require('./config.json');
const bodyParser = require("body-parser");
const app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cookieParser());

app.use('/users', require('./controller/user.controller'));

app.use('/relationships', require('./controller/relationship.controller'));
const portServer =3000;
app.listen(portServer, () => console.log('Server listening on port ' + portServer));