const { CloudTasksClient } = require("@google-cloud/tasks");
const config = require("./config");
const logger = require("./logger");

const { location, queueName } = config.tasks;

const client = new CloudTasksClient();

const init = async () => {
  const response = {};
  try {
    const project = await client.getProjectId();
    const locationPath = client.locationPath(project, location);
    const queuePath = client.queuePath(project, location, queueName);
    logger.info(`Creating Cloud Tasks queue: ${queuePath}`);
    const [queue] = await client.createQueue({
      parent: locationPath,
      queue: {
        name: queuePath,
        appEngineHttpQueue: {
          appEngineRoutingOverride: {
            service: "default",
          },
        },
      },
    });
    response.queue = queue;
  } catch (error) {
    logger.error(`Error creating queue: ${error.message}`);
    response.error = error;
  }
  return response;
};

const queueRequest = async (request) => {
  try {
    const project = await client.getProjectId();
    const queuePath = client.queuePath(project, location, queueName);
    const body = JSON.stringify(request);
    logger.info(`Queueing task on queue: ${queuePath} with data: ${body}`);
    const [response] = await client.createTask({
      parent: queuePath,
      task: {
        appEngineHttpRequest: {
          httpMethod: "POST",
          relativeUri: "/task",
          body: Buffer.from(body).toString("base64"),
        },
      },
    });
    return response;
  } catch (error) {
    logger.error(`Error creating task: ${error.message}`);
    return error;
  }
};

module.exports = {
  init,
  queueRequest,
};
