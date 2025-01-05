# MSfFP Product Mgmt - Miscroservice Skeleton for Fast Prototyping

## Intro
Simple ProductMgmt microservice to create/update products and publish/produce domain events about the changes to be part of an event-driven system. 

## Key features:
* create/update products via REST API
* SwaggerUI to access REST API via browser at http://localhost:8090/swagger-ui.html
* store products in a relational database
* protect against concurrent modifications via VERSION field pattern
* publich domain event (business event) messages about product changes for consumers
* transactional OUTBOX pattern to publis domain event messages atomically (with eventual consistency)

## Bugs
* ProductChanged business event (domain event) in Kafka contains wrong (the previous) version attribute
