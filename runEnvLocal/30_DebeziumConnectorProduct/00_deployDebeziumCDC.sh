# Examples
# https://debezium.io/documentation/reference/stable/transformations/outbox-event-router.html
# https://developer.confluent.io/courses/kafka-connect/rest-api/
# https://debezium.io/blog/2020/01/22/outbox-quarkus-extension/


# curl -X POST -d "@outboxSourceConnector.json" http://localhost:8083/connectors

CONNECTHOST="connect"
if [ $(hostname) -ne "productCDC" ]
then
    CONNECTHOST=localhost
fi    


curl --silent -i -X PUT -H  "Content-Type:application/json" \
    http://${CONNECTHOST}:8083/connectors/product-postgres-outbox-source-connector/config \
    -d '{
            "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
            "plugin.name": "pgoutput",
            "database.hostname": "postgresProduct",
            "database.port": "5432",
            "database.dbname": "postgres",
            "database.user": "postgres",
            "database.password": "password",
            "topic.prefix": "MSfFP", 
            "table.include.list": "public.outbox",
            "tombstones.on.delete": "false",
            "transforms": "outbox",
            "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
            "transforms.outbox.table.field.event.id": "event_id",
            "transforms.outbox.table.field.event.key": "aggregate_id",
            "transforms.outbox.table.fields.additional.placement": "aggregate_id:partition",
            "transforms.outbox.table.fields.additional.placement": "event_id:header:EventID,event_type:header:EventType,created_at:header:CreatedAt,custom_headers:header:CustomHeaders",
            "transforms.outbox.table.expand.json.payload": true,
            "value.converter": "org.apache.kafka.connect.json.JsonConverter",
            "transforms.outbox.table.op.invalid.behavior": "error",
            "transforms.outbox.route.topic.replacement": "MSfFP.ProductMgmt.event.${routedByValue}", 
            "transforms.outbox.route.by.field": "aggregate_type",
            "transforms.outbox.route.tombstone.on.empty.payload": true
    }'


echo "Kafka Connect cluster:"
curl --silent localhost:8083

echo "\n\n\nPlugins"
curl -s localhost:8083/connector-plugins

echo "\n\n\nConnectors:"
curl --silent localhost:8083/connectors
