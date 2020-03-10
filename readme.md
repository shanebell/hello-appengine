# Hello App Engine

This is a simple Google App Engine web application using NodeJS 10.

To deploy this to a new Google Cloud project:

- Create a brand new Google Cloud Project
- Launch the Google Cloud Shell
- Run the following commands:
    - `gcloud app create --region australia-southeast1`
    - `gcloud services enable cloudtasks.googleapis.com`
    - `git clone https://github.com/shanebell/hello-appengine.git`
    - `cd hello-appengine`
    - `gcloud app deploy`
- Once the deployment is complete you can confirm the application is running by navigating to `App Engine > Versions` in the Google Cloud Console. Click on the link under `Version` to open the application in your browser.
- Bootstrap the application by requesting `https://<PROJECT_ID>.appspot.com/bootstrap`
- You can request any HTTP method on any path of the application and the request details will be captured and sent to BigQuery. Note that the following paths are special and will not trigger the request to be logged:
    - `GET /`
    - `GET /bootstrap`
    - `POST /task`

## Local dev
To run the app locally:
- Follow the instructions [here](https://cloud.google.com/docs/authentication/getting-started) to download a service account credential file and save in the local directory as `credentials.json` 
- Install npm dependencies: `npm i`
- Run the application on port 8080: `GOOGLE_APPLICATION_CREDENTIALS=./credentials.json npm start`

## Load test
To load test the app and see App Engine auto-scaling, you can run: 
- `seq 1000 | parallel -j 60 curl -s -o /dev/null https://<PROJECT_ID>.appspot.com/test`