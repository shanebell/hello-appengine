const tasks = require("./tasks");
const bigquery = require("./bigquery");
const firestore = require("./firestore");
const logger = require("./logger");
const express = require("express");
const { DateTime } = require("luxon");
const bodyParser = require("body-parser");
const _ = require("lodash");

// paths to ignore when logging inbound requests
const EXCLUDED_PATHS = ["/favicon.ico"];

const app = express();
app.set("view engine", "ejs");

// middleware
app.enable("trust proxy");
app.use(bodyParser.raw({ type: "application/octet-stream" }));

const handleAsync = (handler) => (req, res, next) => {
  handler(req, res, next).catch((error) => {
    next(error);
  });
};

// Bootstrap application to create tasks queues, datasets etc
app.get("/_ah/warmup", async (req, res) => {
  logger.info("Warm up request...");
  const response = await Promise.all([bigquery.init(), tasks.init()]);
  res.send(response);
});

// Landing page
app.get(
  "/",
  handleAsync(async (req, res) => {
    const requests = await firestore.listRequests(10);
    res.render("index", { requests });
  })
);

// Task handler to process incoming messages from Google Cloud Tasks
app.post(
  "/task",
  handleAsync(async (req, res) => {
    const data = JSON.parse(req.body);
    logger.info("Received message from task queue: %o", data);
    const response = await bigquery.saveRequest(data);
    res.send({ response });
  })
);

// For all other paths capture the incoming request and store synchronously in Firestore and asynchronously in BigQuery (via a task queue)
app.all(
  "/*",
  handleAsync(async (req, res) => {
    // wait for 'w' millis to simulate a delay
    const waitPeriod = parseInt(req.query.w);
    if (Number.isInteger(waitPeriod)) {
      logger.info(`Sleeping for ${waitPeriod}ms`);
      await new Promise((resolve) => setTimeout(resolve, waitPeriod));
    }

    if (!EXCLUDED_PATHS.includes(req.path)) {
      const data = {
        date: DateTime.utc().toString(),
        protocol: req.protocol,
        method: req.method,
        hostname: req.hostname,
        ip: req.ip,
        path: req.path,
        query: _.map(req.query, (value, name) => ({ name, value })),
        headers: _.map(req.headers, (value, name) => ({ name, value })),
      };
      await Promise.all([firestore.saveRequest(data), tasks.queueRequest(data)]);
    }

    res.redirect("/");
  })
);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  logger.info(`Application listening on port ${PORT}`);
});
