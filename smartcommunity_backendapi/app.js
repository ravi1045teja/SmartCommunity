const Express = require("express");
const BodyParser = require("body-parser");
const MongoClient = require("mongodb").MongoClient;
const cors = require("cors");
var app = Express();

//mongoDB connection
const CONNECTION_URL = "mongodb+srv://admin:admin@smartcommunity-gxeha.mongodb.net/test?retryWrites=true&w=majority"
app.use(BodyParser.json());
app.use(cors())
app.use(BodyParser.urlencoded({ extended: true }));
var database, collection, col, col1;
// listen to server 9500
app.listen(9500, () => {
    MongoClient.connect(CONNECTION_URL, { useNewUrlParser: true, useUnifiedTopology: true }, (error, client) => {
        if (error) {
            throw error;
        }
        database = client.db("database");
        collection = database.collection("problems");
        col = database.collection("busSurvey");
        col1 = database.collection("covSurvey");
        console.log("Connected to database !");
    });
});

// post problem recipe
app.post('/problem', (req, res) => {

        const problem = {
            location: req.body.location,
            type: req.body.type,
            description: req.body.description
        }
        console.log(problem);


        collection.insertOne(problem, (err, result) => {
            if (err) {
                return res.status(400).send(err);
            }
            res.status(200).send(result);
        })


    })
    // get problem API
app.get('/problem', (req, res) => {
    collection.find().toArray(function(err, result) {
        if (err) {
            return res.status(500).send(err);
        }
        res.send(result);
    })
})

// problem by type

app.get('/problem/:type', (req, res) => {
    const type = req.params.type;
    collection.find({ 'type': type }).toArray(function(err, result) {
        if (err) {
            return res.status(500).send(err);
        }
        res.send(result)
    })
})

// problem by location
app.get('/problem/:location', (req, res) => {
    const location = req.params.location;
    collection.find({ 'location': location }).toArray(function(err, result) {
        if (err) {
            return res.status(500).send(err);
        }
        res.send(result)
    })
})


// post API for submitting bussurvey
app.post('/busSurvey', (req, res) => {
    const bus = {
        'first': req.body.first,
        'second': req.body.second,
        'third': req.body.third,
        'fourth': req.body.fourth,
        'fifth': req.body.fifth,
        'sixth': req.body.sixth
    }

    col.insertOne(bus, (err, result) => {
        if (err) {
            return res.status(400).send(err);
        }
        res.status(200).send(result);
    })


})

// post api to submit covid 19 survey
app.post('/covSurvey', (req, res) => {
    const cov = {
        'first': req.body.first,
        'second': req.body.second,
        'third': req.body.third,
        'fourth': req.body.fourth,
        'fifth': req.body.fifth,
        'sixth': req.body.sixth
    }

    col1.insertOne(cov, (err, result) => {
        if (err) {
            return res.status(400).send(err);
        }
        res.status(200).send(result);
    })


})

// get api to get responses for bussurvey

app.get('/busSurvey', (req, res) => {
    col.find().toArray(function(err, result) {
        if (err) {
            return res.status(500).send(err);
        }
        res.send(result)
    })

})

// get api to get responses for covid19 survey
app.get('/covSurvey', (req, res) => {
    col1.find().toArray(function(err, result) {
        if (err) {
            return res.status(500).send(err);
        }
        res.send(result)
    })

})