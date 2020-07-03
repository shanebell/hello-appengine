const { Firestore } = require("@google-cloud/firestore");
const config = require("./config");
const logger = require("./logger");

const { collectionPath } = config.firestore;

const client = new Firestore();

const listRequests = async (limit) => {
  logger.info(`Listing the ${limit} most recent requests`);
  const results = await client.collection(collectionPath).orderBy("date", "desc").limit(limit).get();
  const requests = [];
  results.forEach((result) => requests.push(result.data()));
  return requests;
};

const saveRequest = async (data) => {
  logger.info(`Saving request with data: ${JSON.stringify(data)}`);
  try {
    const document = client.collection(collectionPath).doc();
    await document.set(data);
    const doc = await document.get();
    return doc.data();
  } catch (error) {
    logger.error(`Error saving request: ${error.message}`);
    return error;
  }
};

module.exports = {
  listRequests,
  saveRequest,
};
