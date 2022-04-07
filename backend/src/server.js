if(process.env.NODE_ENV !== "production"){
    // require("dotenv").config();
}

const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const path = require("path");

const app = express();


const corsOptions = {
    origin : "http://localhost:3000"
}
app.use(cors(corsOptions));


global.appRoot = path.resolve("./");


app.use(bodyParser.urlencoded({ extended: true } ));
app.use(bodyParser.json());

app.set("port",8080);

app.use("/api/",require("./routes/api"));


app.listen(app.get("port"), () => {
    console.log("app started on https://localhost:"+app.get("port"));
});