#!/bin/sh
set -e

awslocal dynamodb create-table \
  --cli-input-json file:///etc/localstack/ddl/orders-table.json || true
