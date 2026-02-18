package com.example.hello_world.repository;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import com.example.hello_world.config.DynamoDbProperties;
import com.example.hello_world.constant.DynamoDbConst;
import com.example.hello_world.criteria.OrderQueryByGsiOneCriteria;
import com.example.hello_world.entity.OrderEntity;

import lombok.RequiredArgsConstructor;
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
 * ordersテーブルアクセス用クラス.
 */
@Repository
@RequiredArgsConstructor
public class OrderRepository implements InitializingBean{

    // DynamoDBの変数定義取得用
    private final DynamoDbProperties props;

    // DynamoDBクライアント
    private final DynamoDbEnhancedClient client;

    // ordersテーブルオブジェクト
    private DynamoDbTable<OrderEntity> orderTable;

    // GSIオブジェクト
    private DynamoDbIndex<OrderEntity> gsi1;
    private DynamoDbIndex<OrderEntity> gsi2;
    private DynamoDbIndex<OrderEntity> gsi3;

    // 削除フラグがfalseのExpression
    private static final Expression DELETED_EXPRESSION = Expression.builder()
            .expression("deleted = :deleted")
            .putExpressionValue(":deleted", AttributeValue.builder().bool(false).build())
            .build();

    @Override
    public void afterPropertiesSet() {

        this.orderTable = client.table(
            props.getTables().getOrders(),
            TableSchema.fromBean(OrderEntity.class)
        );
        this.gsi1 = this.orderTable.index(DynamoDbConst.ORDERS_GSI1_NAME);
        this.gsi2 = this.orderTable.index(DynamoDbConst.ORDERS_GSI2_NAME);
        this.gsi3 = this.orderTable.index(DynamoDbConst.ORDERS_GSI3_NAME);
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
    public List<OrderEntity> queryByGsi1(OrderQueryByGsiOneCriteria criteria) {
        
        String userId = criteria.getUserId();
        String fromCreatedAt = criteria.getFromDateTime().toString();
        String toCreatedAt = criteria.getToDateTime().toString();
        
        // 検索条件の作成
        QueryConditional conditional = QueryConditional
                .sortBetween(
                    k -> k.partitionValue(userId).sortValue(fromCreatedAt), 
                    k -> k.partitionValue(userId).sortValue(toCreatedAt)
                );
        
        // クエリリクエスト作成
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .filterExpression(DELETED_EXPRESSION)  // 削除フラグ=false
                .scanIndexForward(true)  // 昇順
                .build();
        
        // クエリ実行・結果取得
        return gsi1.query(request)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    
}
