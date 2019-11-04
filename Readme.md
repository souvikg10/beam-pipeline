# Apache Beam example

## Pre-requisites
- Java 8
- JDK
- Maven

## Installation 

```
$ Java --version
$ brew update
$ brew install maven
```
## Execution Direct Runner
```
mvn compile exec:java -Dexec.mainClass=tweetAnalysis.App \
     -Dexec.args="--inputFile=pom.xml --output=counts" -Pdirect-runner
```

## Execution Spark Local mode
```
mvn compile exec:java -Dexec.mainClass=tweetAnalysis.App \
     -Dexec.args="--runner=SparkRunner --inputFile=pom.xml --output=data/counts" -Pspark-runner
```
## Execution Flink Cluster
```
mvn package exec:java -Dexec.mainClass=tweetAnalysis.App \
     -Dexec.args="--runner=FlinkRunner --flinkMaster=<flink master> --filesToStage=target/tweet-Analysis-App-0.1.jar \
                  --inputFile=pom.xml --output=/data/counts" -Pflink-runner
```
