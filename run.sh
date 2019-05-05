#!/usr/bin/env bash

user=postgres
password=admin
connectionUrl=jdbc:postgresql://localhost:5432/postgres?currentSchema\=public

export UM_DB_USERNAME=${user}
export UM_DB_PASSWORD=${password}
export UM_DB_CONNECTION_URL=${connectionUrl}

# run db in docker
echo "Pulling postgres docker image"
docker pull postgres:11
echo "Starting postgres"
docker run --name user-management-db -e POSTGRES_PASSWORD=${password} -d -p 5432:5432 postgres:11

# check db is up and run application
for count in {1..100}; do
      echo "Pinging database attempt "${count}
      if  $(nc -z localhost 5432) ; then
        echo "Running user-management"
        ./gradlew bootRun
        break
      fi
      sleep 5
done