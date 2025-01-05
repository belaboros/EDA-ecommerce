clear

echo "Kafka Connect cluster:"
curl --silent localhost:8083 | jq '.'

echo "\n\n\nPlugins"
curl -s localhost:8083/connector-plugins | jq '.'

echo "\n\n\nConnectors:"
curl --silent localhost:8083/connectors | jq '.'

echo "\n\n\npostgres-outbox-source-connector"
curl --silent localhost:8083/connectors/product-postgres-outbox-source-connector | jq '.'

