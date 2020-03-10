# Hello App Engine

This is a simple Google App Engine web application using Java 8.

To deploy this to a new Google Cloud project follow these steps:

- Create a brand new Google Cloud Project
- Launch the Google Cloud Shell
- Run the following commands
    - `gcloud app create`
    - `git clone https://github.com/3wks/hello-appengine.git`
    - `cd hello-appengine`
    - `gcloud services enable cloudtasks.googleapis.com`
    - `npm i`
    - `gcloud app deploy`
- Once the deployment is complete you can confirm the application is running by navigating to `App Engine > Versions` in the Google Cloud Console. Click on the link under `Version` to open the application in your browser.
- Bootstrap the application by requesting `/bootstrap`
- You can request any HTTP method on any path of the application and the request details will be captured and sent to BigQuery. Note that the following paths are special and will not trigger the request to be logged:
    - `GET /`
    - `GET /bootstrap`
    - `POST /task`

## Local dev
To test the app locally, open your local terminal and run: 
- `npm i`
- `npm start`

## Load test
To load test the app and see App Engine auto-scaling, you can run: 
- `seq 1000 | parallel -j 60 curl -s -o /dev/null https://<myappid>.appspot.com/test`