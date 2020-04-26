#!/bin/bash

docker stop $(docker ps | grep "jpsx:latest" | cut -d " " -f1)
docker rm $(docker ps -a | grep "jpsx:latest" | cut -d " " -f1) 

docker run -it -d --entrypoint bash -v build:/usr/src/jpsx/build jpsx:latest

containerid=$(docker ps | grep "jpsx:latest" | cut -d " " -f1)
docker cp ${containerid}:/usr/src/jpsx/ship/ ./
