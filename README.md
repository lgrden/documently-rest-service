# documently-microservice @ We Get IT

## Descriprion
This microservice serves prepared document templates.
It allows users in easy way to reuse those templates by only changing dynamic variables e.g. recipient.

One of the example of template document is letter of authorization.
A letter of authorization is a document authorizing the recipient to carry out a certain action.
For example a bank account holder may write a letter to the bank authorizing a transaction, or authorizing somebody else to act on their behalf if sick.

## Requirenments
 - JDK 11+
 - Maven 3.6.1+

## Build tools
  - clean and build project
        ```
        mvn clean install
        ```
  - start project
        ```
        mvn spring-boot:run -Dspring.config.location=localhost.properties
        ```