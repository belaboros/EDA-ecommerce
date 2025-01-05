# MSfFP Order Mgmt - Miscroservice Skeleton for Fast Prototyping

## Intro
Simple OrderMgmt microservice to create/update orders and publish/produce domain events about the changes to be part of an event-driven system. 

## Key features:
* subscribes and consumer Product:Changed domain event (business event messages)
* maintains a local replica of products in its relational database
* create/update orders via REST API
* store orders in a relational database
* protect against concurrent modifications via VERSION field pattern
* publich domain event (business event) messages about order changes for consumers
* transactional OUTBOX pattern to publis domain event messages atomically (with eventual consistency)


## TODO
* add DLQ for invalid messages
* schema evolution & FORWARD compatibility on messaging channels
* Message schema should follow the API-first approach and come from SchemaRegistry for Product Domain Event Messages
* Messages schema for Order domain event messages shall use source-code first
* unit/integration tests for
  * consurrent modification
  * out-of-order messages