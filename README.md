# EDA ecommerce

## About
* hypothetical e-Commerce system with event-driven ProductMgmt, OrderMgmt, ... microservices
* fast prototypes for some EDA (Event-Driven rchitecture) related capabilities

## Key features
* publish domain event messages atomically (via transactional OUTBOX pattern)
* consume domain event messages to react/respond/update local state/create derived events/...
* REST API to play with the services
* SwaggerUI to access REST API via browser
* RDBMS stateful storage
* protect against concurrent modifications via VERSION field pattern

## NOTE: only for experimentations
This git repo is only for experimentation and fast prototyping certain features.
It is in a constant evolving & unfinished state.
It is not for production use.

## Technology stack
* Java 23
* SpringBoot 3.4
* Postgres latest
* Debezium & OUTBOX Event Router
* Confluent Kafka 
* Gradle
* Docker compose

## Run/debug from IDE

1. run the environment locally
```
runLocalEnv/01_startEnv.sh
```

2. run the ProductApp.java from IDE
3. run the OrderApp.java from IDE

## Useful links

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.0/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.0/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.0/reference/actuator/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
