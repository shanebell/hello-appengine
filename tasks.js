const {CloudTasksClient} = require('@google-cloud/tasks');
const logger = require('./logger');

const client = new CloudTasksClient();

const createQueue = async (location, queue) => {
    try {
        const project = await client.getProjectId();
        const locationPath = client.locationPath(project, location);
        const queuePath = client.queuePath(project, location, queue);
        logger.info(`Creating queue: '${queuePath}'`);
        const [response] = await client.createQueue({
            parent: locationPath,
            queue: {
                name: queuePath,
                appEngineHttpQueue: {
                    appEngineRoutingOverride: {
                        service: 'default',
                    },
                },
            },
        });
        return response;
    } catch (error) {
        logger.error('Error creating queue: %o', error);
        return error;
    }
};

const createTask = async (location, queue, body) => {
    try {
        const project = await client.getProjectId();
        const queuePath = client.queuePath(project, location, queue);
        logger.info(`Creating task on queue '${queuePath}' with body '${body}'`);
        const [response] = await client.createTask({
            parent: queuePath,
            task: {
                appEngineHttpRequest: {
                    httpMethod: 'POST',
                    relativeUri: '/task',
                    body: Buffer.from(body).toString('base64'),
                },
            },
        });
        return response;
    } catch (error) {
        logger.error('Error: %o', error);
        return error;
    }
};

module.exports = {
    createQueue,
    createTask,
};