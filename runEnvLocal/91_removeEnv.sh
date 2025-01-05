# https://docs.confluent.io/platform/current/get-started/platform-quickstart.html

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd ) #"
cd ${SCRIPT_DIR}

docker compose stop
docker compose rm --force
#docker system prune -a --volumes --filter "label=io.confluent.docker"
