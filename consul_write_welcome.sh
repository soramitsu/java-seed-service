#!/usr/bin/env bash

function consul(){
  docker-compose exec consul consul "$@"
}

consul kv put config/example1/custom/welcome $1
