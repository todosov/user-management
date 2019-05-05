# User-management

Simple java application which provides CRUD API for user realm.
Application can consumes both json and xml formats.
To specify required response content type set *Accept* header to either application/json or application/xml.

# Stack of technologies

 - Gradle as build tool
 - Spring Boot as main Framework
 - Postgres SQL as data base
 - Flyway for db migration control
 
# Tests

There are tests for web, persistence and service layers.
In tests embedded h2 db is used instead of postgres db. 

# Deployment 

Application builds docker image and pushes it to docker hub. Image can be used for further deployment. Versioning of application image is not implemented.
To build and push image run 
```sh 
./gradlew dockerPushImage
```

# How to run

Execute 
```sh 
docker-compose up
```
in the project's root directory. 
Docker will run containers with application and postgres db.
DB schema will be automatically rolled out by Flyway.

Application uses environment variables to establish db connection: 
 ```sh 
 $UM_DB_CONNECTION_URL
 $UM_DB_USERNAME
 $UM_DB_PASSWORD
 ``` 
 
Environment variables used during build:
 ```sh 
 $DOCKER_USER #docker hub repository and username
 $DOCKER_PASS #docker hub password
 ``` 
 
# Request examples

Create new user:
```sh
curl -X POST http://localhost:8080/service/user/realm \
  -H 'Accept: application/json,*/*' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "some name",
    "description": "some description"
    }' \
    -i
 ```
  
Retrieve existing user:
```sh
curl -X GET http://localhost:8080/service/user/realm/1 \
  -H 'Accept: application/json' \
  -i
```