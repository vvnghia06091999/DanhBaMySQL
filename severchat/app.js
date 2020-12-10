//const express = require("express");
const cookieParser = require('cookie-parser');
const bodyParser = require("body-parser");
var app = require('express')();

var http = require('http').createServer(app);
var io = require('socket.io')(http);

io.on('connection', (socket) => {
    console.log('a user connected');
    socket.on("Update",function(data){
        console.log(data);
        io.emit("Server",{tinNhan: data});
    });
    socket.on("GuiKetBan",function(data){
        console.log(data);
        io.emit("GuiKetBan",{tinNhan: data});
    });
    socket.on("ChapNhanKetBan",function(data){
        console.log(data);
        io.emit("ChapNhanKetBan",{tinNhan: data});
    });
});

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cookieParser());

app.use('/users', require('./controller/user.controller'));
app.use('/relationships', require('./controller/relationship.controller'));

//const portServer =3000;
//app.listen(portServer, () => console.log('Server listening on port ' + portServer));
http.listen(3000, () => {
    console.log('listening on *:3000');
  });