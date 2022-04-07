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


module.exports = router;