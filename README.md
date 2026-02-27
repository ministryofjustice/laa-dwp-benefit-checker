# laa-dwl-benefit-checker-2.0
[![Ministry of Justice Repository Compliance Badge](https://github-community.service.justice.gov.uk/repository-standards/api/laa-spring-boot-microservice-template/badge)](https://github-community.service.justice.gov.uk/repository-standards/laa-spring-boot-microservice-template)


## Overview

API to check whether someone is on benefits or not.  There are a number of clients that 

### Project Structure
Includes the following subprojects:

- `bc-api` - OpenAPI specification used for generating API stub interfaces and documentation.
- `bc-service` - REST API service.


# Generate JAXB classes
./gradlew xjc

# Test the Ws (after starting server locally)
curl -v --header "Content-Type: text/xml" -d @request.xml http://localhost:8080/ws
# To access WSDL
http://localhost:8080/
# Sort out Gradle wierdness
./gradlew --stop
rm -rf build .gradle
./gradlew build --refresh-dependencies

## Build And Run Application

### Build application
`./gradlew clean build`

### Run integration tests
`./gradlew integrationTest`

### Run application
`./gradlew bootRun`

### Run application via Docker
`docker compose up`

## Application Endpoints

### API Documentation

#### Swagger UI
- http://localhost:8080/swagger-ui/index.html
#### API docs (JSON)
- http://localhost:8080/v3/api-docs

### Actuator Endpoints
The following actuator endpoints have been configured:
- http://localhost:8080/actuator
- http://localhost:8080/actuator/health

## Additional Information

### Libraries Used
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html) - used to provide various endpoints to help monitor the application, such as view application health and information.
- [Spring Boot Web](https://docs.spring.io/spring-boot/reference/web/index.html) - used to provide features for building the REST API implementation.
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa.html) - used to simplify database access and interaction, by providing an abstraction over persistence technologies, to help reduce boilerplate code.
- [Springdoc OpenAPI](https://springdoc.org/) - used to generate OpenAPI documentation. It automatically generates Swagger UI, JSON documentation based on your Spring REST APIs.
- [Lombok](https://projectlombok.org/) - used to help to reduce boilerplate Java code by automatically generating common
  methods like getters, setters, constructors etc. at compile-time using annotations.
- [MapStruct](https://mapstruct.org/) - used for object mapping, specifically for converting between different Java object types, such as Data Transfer Objects (DTOs)
  and Entity objects. It generates mapping code at compile code.
- [H2](https://www.h2database.com/html/main.html) - used to provide an example database and should not be used in production.


