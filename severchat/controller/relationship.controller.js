const express = require('express');
const router = express.Router();
const { Op } = require("sequelize");

const db = require('../_helper/db');

router.get('/:id', getMyFriend);
router.get('/FriendRequets/:id', getFriendRequets);
router.post('/', createReltionship);
router.get('/getRelationship/:idUser1/:idUser2',getRelationship);
router.put('/:idUser1/:idUser2', updateRelationship);
router.delete('/:idUser1/:idUser2', deleteFriend);
module.exports = router;

function getRelationship(req,res){
  const idUser1 = req.params.idUser1;
  const idUser2 = req.params.idUser2;
  db.Relationship.findOne(
    {where: {
      [Op.or]: [
        {[Op.and]:[
          {idUser1 : idUser1},
          {idUser2 : idUser2}
        ]},
        {[Op.and]:[
          {idUser1 : idUser2},
          {idUser2 : idUser1}
        ]}
      ]
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

function getFriendRequets(req,res){
  const id = req.params.id;
    db.Relationship.findAll(
        {where: {
            [Op.and]: [
                {[Op.or]: [{idUser1 : id},{idUser2 : id}]},
                {status: "2"},
                {idUserAction: {
                  [Op.notLike]: id
                }}
                // 1 Ban Be , 2 Cho Chap Nhan , 3 Chan Ban
            ]
            }
        }
    )
    .then(data => {
      res.send(data);
    })
    .catch(err => {
      res.status(500).send({
        message:
          err.message || "Ko tim duoc."
      });
    });
};
function getMyFriend(req, res) {
    const id = req.params.id;
    db.Relationship.findAll(
        {where: {
            [Op.and]: [
                {[Op.or]: [{idUser1 : id},{idUser2 : id}]},
                {status: "1"}
                // 1 Ban Be , 2 Cho Chap Nhan , 3 Chan Ban
            ]
            }
        }
    )
    .then(data => {
      res.send(data);
    })
    .catch(err => {
      res.status(500).send({
        message:
          err.message || "Ko tim duoc."
      });
    });
};
function createReltionship(req,res){
    const relationship = {
        idUser1: req.body.idUser1,
        idUser2: req.body.idUser2,
        idUserAction: req.body.idUserAction,
        status: req.body.status
    };
    db.Relationship.create(relationship).then(data=>{
        res.send(data);
    }).catch(err => {
        res.status(500).send({
          message:
            err.message || "Them khong thanh cong."
        });
      });
};
function updateRelationship(req,res){
  const idUser1 = req.params.idUser1;
  const idUser2 = req.params.idUser2;
  //  const relationship ={
  //    idUserAction: req.body.idUserAction,
  //    status: req.body.status
  //  };
  db.Relationship.update(req.body,
    {where: {
      [Op.and]: [
        {idUser1: idUser1},
        {idUser2: idUser2}
      ]
    }})
  //   .then(data=>{
  //     res.send(data);
  // }).catch(err => {
  //     res.status(500).send({
  //       message:
  //         err.message || "Cap Nhat Khong thanh cong."
  //     });
  //   });
    .then(num => {
      if (num == 1) {
        res.send({
          message: "Cap Nhat Thanh Cong."
    });
    } else {
      res.send({
        message: `Cap Nhat That Bai`
      });
    }
  }).catch(err => {
    res.status(500).send({
      message: "Khong the cap nhat "
    });
  });
};
function deleteFriend(req,res){
  const idUser1 = req.params.idUser1;
  const idUser2 = req.params.idUser2;
  db.Relationship.destroy({where: {
    [Op.and]: [
      {idUser1: idUser1},
      {idUser2: idUser2}
    ]
  }}).then(num => {
    if (num == 1) {
      res.send({
        message: "Xoa Thanh Cong."
  });} 
    else {
      res.send({
        message: `Xoa That Bai`
    });
  }}).catch(err => {
    res.status(500).send({
      message: "Khong the xoa "
  });
});
};
