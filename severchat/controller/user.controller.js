const express = require('express');
const router = express.Router();

const db = require('../_helper/db');

router.get('/', getAll);
router.post('/', addUser);
router.get('/:id', getByID);
router.get('/getUseByPhone/:phone', getByPhone);

module.exports = router;

function getByPhone(req,res){
  const phone = req.params.phone;
  db.User.findOne(
    {where: {
      phone: phone
    }}
  ).then(data => {
    res.send(data);
  })
  .catch(err => {
    res.status(500).send({
      message:
        err.message || "Ko tim duoc."
    });
  });
}

function getAll(req, res) {
    db.User.findAll()
    .then(data => {
      res.json(data);
    })
    .catch(err => {
      res.status(500).send({
        message:
          err.message || "Ko tim duoc."
      });
    });
};
function addUser(req,res){
    const user = {
        phone: req.body.phone,
        email: req.body.email,
        fullName: req.body.fullName
    };
    db.User.create(user).then(data=>{
        res.send(data);
    }).catch(err => {
        res.status(500).send({
          message:
            err.message || "Them khong thanh cong."
        });
      });
}
function getByID(req,res){
    const id = req.params.id;

    db.User.findByPk(id).then(data => {
        res.send(data);
      })
      .catch(err => {
        res.status(500).send({
          message: "Error retrieving User with id=" + id
        });
      });
};