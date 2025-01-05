#!/bin/bash
# See also
# - https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes


clear

IMAGE_NAME='belaboros/eda-ecommerce-product-mgmt'


IDS=$(docker ps -a --filter ancestor=${IMAGE_NAME} --format="{{.ID}}")
if [ ! -z "${IDS}" ]; then
    echo "Removing docker containers of Docker image: ${IMAGE_NAME}"
    echo ${IDS}
    docker rm $(docker stop $(docker ps -a --filter ancestor=${IMAGE_NAME} --format="{{.ID}}"))
else
    echo "There are no Docker containers for image: ${IMAGE_NAME}"
fi

echo -e "\n\nRemoving docker image: ${IMAGE_NAME}"
docker rmi ${IMAGE_NAME}
