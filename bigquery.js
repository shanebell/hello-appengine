const {BigQuery} = require('@google-cloud/bigquery');
const logger = require('./logger');

const client = new BigQuery();

const createDataset = async (datasetName) => {
    try {
        logger.info(`Creating dataset: '${datasetName}'`);
        const [dataset, response] = await client
            .dataset(datasetName)
            .create();
        return response;
    } catch (error) {
        logger.error('Error creating dataset: %o', error);
        return error;
    }
};

const createTable = async (datasetName, tableName, schema) => {
   try {
       logger.info(`Creating table: '${datasetName}.${tableName}'`);
       const [table, response] = await client
           .dataset(datasetName)
           .table(tableName)
           .create({
               schema,
           });
       return response;
   } catch (error) {
       logger.error('Error creating table: %o', error);
       return error;
   }
};

const insert = async (datasetName, tableName, rows) => {
    try {
        logger.info(`Inserting into table: '${datasetName}.${tableName}'`);
        const [response] = await client
            .dataset(datasetName)
            .table(tableName)
            .insert(rows);
        return response;
    } catch (error) {
        logger.error('Error inserting data: %o', error);
        return error;
    }
};

module.exports = {
    createDataset,
    createTable,
    insert,
};