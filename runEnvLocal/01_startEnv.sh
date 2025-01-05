#!/bin/bash
# https://docs.confluent.io/platform/current/get-started/platform-quickstart.html

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd ) #"
cd ${SCRIPT_DIR}


docker compose up -d

