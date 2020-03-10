const tasks = require('./tasks');
const bigquery = require('./bigquery');
const logger = require('./logger');
const fs = require('fs');
const express = require('express');
const {DateTime} = require("luxon");
const bodyParser = require('body-parser');
const _ = require('lodash');

const app = express();

// cloud tasks config
const location = 'australia-southeast1';
const queue = 'default';

// big query config
const dataset = 'hello_appengine';
const table = 'request_data';

// middleware
app.enable('trust proxy');
app.use(bodyParser.raw({type: 'application/octet-stream'}));

// Simple welcome page
app.get('/', (req, res) => {
    res.send('Hello App Engine');
});

// Bootstrap application to create queues, tables etc
app.get('/bootstrap', async (req, res) => {
    const createQueueResponse = await tasks.createQueue(location, queue);
    const createDatasetResponse = await bigquery.createDataset(dataset);
    const schema = JSON.parse(fs.readFileSync('./request_data.json', 'utf8'));
    const createTableResponse = await bigquery.createTable(dataset, table, schema);
    res.send({
        createQueueResponse,
        createDatasetResponse,
        createTableResponse,
    });
});

// Task handler to process incoming messages from Google Cloud Tasks
app.post('/task', async (req, res) => {
    const data = JSON.parse(req.body);
    logger.info('Received message from task queue: %o', data);
    const response = await bigquery.insert(dataset, table, [data]);
    res.send({response});
});

// For all other request paths capture the request details and queue a task
app.all('/*', async (req, res) => {
    logger.info('Queueing task with request data');
    const data = {
        date: DateTime.local().toString(),
        protocol: req.protocol,
        method: req.method,
        hostname: req.hostname,
        ip: req.ip,
        path: req.path,
        headers: _.map(req.headers, (value, name) => ({name, value})),
    };
    const response = await tasks.createTask(location, queue, JSON.stringify(data, null, 2));
    res.send(response);
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    logger.info(`Application listening on port ${PORT}`);
});