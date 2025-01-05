cd ..
clear


#SERVICE="postgres"
#SERVICE="broker"
SERVICE="connect"
#SERVICE="schema-registry"
#SERVICE="control-center"
#SERVICE="ksqldb-server"
#SERVICE="ksqldb-cli"
#SERVICE="ksql-datagen"
#SERVICE="rest-proxy"


docker compose logs ${SERVICE}