FROM adoptopenjdk/openjdk11:latest
COPY target/documently-microservice.jar /
CMD java -jar /documently-microservice.jar