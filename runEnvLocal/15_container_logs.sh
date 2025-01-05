#!/bin/bash

clear


#SERVICE="postgres"
#SERVICE="broker"
#SERVICE="connect"
#SERVICE="schema-registry"
#SERVICE="control-center"
#SERVICE="ksqldb-server"
#SERVICE="ksqldb-cli"
#SERVICE="ksql-datagen"
#SERVICE="rest-proxy"
#docker compose logs ${SERVICE}


# collect Docker container names
CONTAINERS=($(docker compose ps --all --format json | jq '.Name'))
if (( ${#CONTAINERS[@]} == 0 )); then
    echo "There are no running Docker containers"
    echo "Use \"docker compose up\" to start up containers."
    exit 1
fi
# remove quotes
CONTAINERS=("${CONTAINERS[@]//\"/}")


echo "Available Docker containers:"
for i in "${!CONTAINERS[@]}"; do 
  printf "%s:%s\n" "$i" "${CONTAINERS[$i]}"
done


echo -e  "\nSelect container index to view its logs:"
read idx
echo "Logs of container: ${CONTAINERS[$idx]}"
docker compose logs ${CONTAINERS[$idx]}
