# stackrs-service

To run the docker container locally outside of GCP, you need to provide the GOOGLE_APPLICATION_CREDENTIAL. Remembering to map the directory.

```
docker run --env GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account.json -v /path/to:/path/to gcr.io/{project}/{container}
```
