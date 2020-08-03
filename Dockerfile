FROM adoptopenjdk/openjdk11:latest
COPY target/documently-rest-service.jar /
CMD java -jar /documently-rest-service.jar