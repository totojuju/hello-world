#!/bin/sh
set -e

awslocal dynamodb create-table \
  --table-name Orders \
  --attribute-definitions \
    AttributeName=orderId,AttributeType=S \
    AttributeName=userId,AttributeType=S \
    AttributeName=status,AttributeType=S \
    AttributeName=shippingPrefecture,AttributeType=S \
    AttributeName=createdAt,AttributeType=S \
  --key-schema \
    AttributeName=orderId,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes \
    "[
      {
        \"IndexName\": \"gsi_user_created_at\",
        \"KeySchema\": [
          {\"AttributeName\": \"userId\", \"KeyType\": \"HASH\"},
          {\"AttributeName\": \"createdAt\", \"KeyType\": \"RANGE\"}
        ],
        \"Projection\": {\"ProjectionType\": \"ALL\"}
      },
      {
        \"IndexName\": \"gsi_status_created_at\",
        \"KeySchema\": [
          {\"AttributeName\": \"status\", \"KeyType\": \"HASH\"},
          {\"AttributeName\": \"createdAt\", \"KeyType\": \"RANGE\"}
        ],
        \"Projection\": {\"ProjectionType\": \"ALL\"}
      },
      {
        \"IndexName\": \"gsi_prefecture_created_at\",
        \"KeySchema\": [
          {\"AttributeName\": \"shippingPrefecture\", \"KeyType\": \"HASH\"},
          {\"AttributeName\": \"createdAt\", \"KeyType\": \"RANGE\"}
        ],
        \"Projection\": {\"ProjectionType\": \"ALL\"}
      }
    ]" || true
