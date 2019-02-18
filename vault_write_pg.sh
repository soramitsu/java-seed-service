#!/usr/bin/env bash -xe

function vault(){
  docker-compose exec vault vault "$@"
}

source .env

vault kv put -address=http://127.0.0.1:8200 secret/example1 \
  db.username=${POSTGRES_USER} \
  db.password=${POSTGRES_PASSWORD} \
  db.schema=${POSTGRES_SCHEMA} \
  db.database=${POSTGRES_DATABASE} \
  db.host=${POSTGRES_HOST} \
  db.port=${POSTGRES_PORT}
