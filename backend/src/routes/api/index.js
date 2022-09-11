const express = require("express");
const fs = require("fs");
const router = express.Router();



router.get("/", (req,res) => {
    const allData = require("../../../data/json.json");
    res.json(allData);
});

router.post("/", (req,res) => {
    const allData = req.body.allData;
    fs.writeFileSync(appRoot + "/data/json.json",allData);
    res.end();
});


router.get("/contacts", (req,res) => {
    const allData = require("../../../data/contacts.json");
    res.json(allData);
});



router.post("/login", (req,res) => {
    const username = req.body.username;
    const password = req.body.password;

    const users = require("../../../data/users.json");

    if (users["1"].username == username){
        if(users["1"].password == password){
            res.json("PASSED");
        }
        else{
            res.json("incorrect password");
        }
    }
    else{
        res.json("incorrect user");
    }

})


module.exports = router;