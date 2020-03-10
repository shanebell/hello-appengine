const bunyan = require('bunyan');
const {LoggingBunyan} = require('@google-cloud/logging-bunyan');

const logger = bunyan.createLogger({
    name: 'hello-appengine',
    streams: [
        {
            stream: process.stdout,
            level: 'debug',
        },
        new LoggingBunyan().stream('debug'),
    ],
});

module.exports = logger;