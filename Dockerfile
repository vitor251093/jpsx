FROM kmindi/openjdk-ant-docker:latest

COPY . /usr/src/jpsx
WORKDIR /usr/src/jpsx
RUN ant
