module.exports = {
  tasks: {
    location: "australia-southeast1",
    queueName: "default",
  },
  bigQuery: {
    datasetName: "hello_appengine",
    tableName: "requests",
  },
  firestore: {
    collectionPath: "requests",
  },
};
