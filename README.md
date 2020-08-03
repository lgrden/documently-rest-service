# documently-microservice @ We Get IT

## Descriprion
This microservice serves prepared document templates.
It allows users in easy way to reuse those templates by only changing dynamic variables e.g. recipient.

One of the example of template document is letter of authorization.
A letter of authorization is a document authorizing the recipient to carry out a certain action.
For example a bank account holder may write a letter to the bank authorizing a transaction, or authorizing somebody else to act on their behalf if sick.

When spring profile is set to dev, storage is populated with the default data.
You can set the profile: ```spring.profiles.active=dev```

## System requirenments
 - JDK 11+
 - Maven 3.6.1+
 - MongoDB 4.0.10+

## APIs
  - OpenAPI definition: /v3/api-docs
  - Swagger: /swagger-ui.html
  - Management: /monitoring
  
## Build tools
  - clean and build project ```mvn clean install```
  - start project ```mvn spring-boot:run -Dspring.config.location=localhost.properties```
  
## Docker
To run microservice inside a docker container please execute following commands: 
  - build docker image after the project is build ```docker build -t documently-microservice .```
  - run mongo and builded microservice inside a docker ```docker-compose -f docker-compose.yaml up -d```