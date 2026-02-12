package com.example.hello_world.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.hello_world.config.DynamoDbProperties;
import com.example.hello_world.constant.DynamoDbConst;
import com.example.hello_world.entity.OrderEntity;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * Ordersテーブルアクセス用クラス.
 */
@Repository
public class OrderRepository {

    // Ordersテーブルオブジェクト
    private final DynamoDbTable<OrderEntity> orderTable;

    // GSIオブジェクト
    private final DynamoDbIndex<OrderEntity> gsi1;
    private final DynamoDbIndex<OrderEntity> gsi2;
    private final DynamoDbIndex<OrderEntity> gsi3;

    // 削除フラグがfalseのExpression
    private final Expression deletedExpression = Expression.builder()
            .expression("deleted = :deleted")
            .putExpressionValue(":deleted", AttributeValue.builder().bool(false).build())
            .build();

    public OrderRepository(DynamoDbProperties props, DynamoDbEnhancedClient client) {
        DynamoDbTable<OrderEntity> tempTable = client.table(
            props.getTables().getOrders(),
            TableSchema.fromBean(OrderEntity.class)
        );
        this.orderTable = tempTable;
        this.gsi1 = tempTable.index(DynamoDbConst.ORDERS_GSI1_NAME);
        this.gsi2 = tempTable.index(DynamoDbConst.ORDERS_GSI2_NAME);
        this.gsi3 = tempTable.index(DynamoDbConst.ORDERS_GSI3_NAME);
    }
    
    /**
     * OrderIdをキーに1件取得.
     * 
     * @param orderId
     * @return OrderEntity
     */
    public OrderEntity getItemByOrderId(String orderId) {

        GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                .key(k -> k.partitionValue(orderId))
                .build();
        return orderTable.getItem(request);
    }

    /**
     * GSI1を使ってユーザーID＋作成日時範囲で検索.
     * 
     * @param userId
     * @param fromCreatedAt
     * @param toCreatedAt
     * @return List<OrderEntity>
     */
    public List<OrderEntity> queryByGsi1(String userId, String fromCreatedAt, String toCreatedAt) {
        
        // 検索条件の作成
        QueryConditional conditional = QueryConditional
                .sortBetween(
                    k -> k.partitionValue(userId).sortValue(fromCreatedAt), 
                    k -> k.partitionValue(userId).sortValue(toCreatedAt)
                );
        
        // クエリリクエスト作成
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .filterExpression(deletedExpression)  // 削除フラグ=false
                .scanIndexForward(true)  // 昇順
                .build();
        
        // クエリ実行・結果取得
        return gsi1.query(request)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
}
