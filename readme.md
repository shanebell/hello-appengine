# Hello App Engine

This is a simple Google App Engine web application using Java 8.

To deploy this to a new Google Cloud project follow these steps:

- Create a brand new Google Cloud Project
- Launch the Google Cloud Shell
- Run the following commands
    - `gcloud app create`
    - `git clone https://github.com/3wks/hello-appengine.git`
    - `cd hello-appengine`
    - `mvn appengine:deploy`
- Once the deployment is complete you can confirm the application is running by navigating to `App Engine > Versions` in the Google Cloud Console. Click on the link under `Version` to open the application in your browser.
- Launch the Code Editor and make some changes to `src/main/java/hello/servlet/HelloServlet.java`.
- Redeploy the application by running
    - `mvn appengine:deploy`
- Confirm the new version is deployed from `App Engine > Versions` 

## Query parameters
The app supports an optional query string `s` to specify a delay in the response. i.e. `http://localhost:8080?s=1000` will delay the response for 1 sec.

## Local dev
To test the app locally, open your local terminal and run: 
```
mvn appengine:run
```

## Load test
To load test the app and see Appengine austoscaling, you can run: 
```
seq 100000 | parallel -i -j 60 curl -s -o /dev/null -w "{}:" https://<myappid>.appspot.com/?s=1000
```