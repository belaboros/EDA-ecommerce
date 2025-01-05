# Run the environment components locally

Helper scripts in this directory can be used to start/stop/manage Postgres databases, Kafka cluster, CDC connectors, ...
needed for local development.

Kafka and Postgres run in Docker Compose containers.

# Confluent Kafka
To generte a docker-compose.yml file for Confluent Kafka:

```
wget https://raw.githubusercontent.com/confluentinc/cp-all-in-one/7.8.0-post/cp-all-in-one-kraft/docker-compose.yml
```

More info: [Confluent Platform QuickStart](https://docs.confluent.io/platform/current/get-started/platform-quickstart.html)


