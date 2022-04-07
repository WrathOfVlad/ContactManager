const express = require("express");
const fs = require("fs");
const router = express.Router();



router.get("/", (req,res) => {
    const allData = require("../../../data/json.json");
    console.log(allData);
    res.json(allData);
});


module.exports = router;