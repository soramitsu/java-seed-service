#!/usr/bin/env bash

function vault(){
  docker-compose exec vault vault "$@"
}

vault kv put -address http://127.0.0.1:8200 secret/example1 custom.name=$1
