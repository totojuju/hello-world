package com.example.hello_world.entity;

import com.example.hello_world.constant.DynamoDbConst;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@Data
@DynamoDbBean
public class OrderEntity {
    
    // 注文ID
    private String orderId;
    // ユーザーID
    private String userId;
    // ステータス
    private String status;
    // 注文金額
    private Double totalAmount;
    // 配送先都道府県
    private String shippingPrefecture;
    // 作成日時
    private String createdAt;
    // TTL
    private Long ttl;

    @DynamoDbPartitionKey
    public String getOrderId() {
        return orderId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDbConst.ORDERS_GSI1_NAME)
    public String getUserId() {
        return userId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDbConst.ORDERS_GSI2_NAME)
    public String getStatus() {
        return status;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDbConst.ORDERS_GSI3_NAME)
    public String getShippingPrefecture() {
        return shippingPrefecture;
    }

    @DynamoDbSecondarySortKey(indexNames = {DynamoDbConst.ORDERS_GSI1_NAME, 
            DynamoDbConst.ORDERS_GSI2_NAME, DynamoDbConst.ORDERS_GSI3_NAME})
    public String getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute(DynamoDbConst.ATTRIBUTE_TTL)
    public Long getTtl() {
        return ttl;
    }
}
