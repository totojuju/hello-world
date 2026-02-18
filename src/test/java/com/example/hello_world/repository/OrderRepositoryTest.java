package com.example.hello_world.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.hello_world.DynamoTestDataLoader;
import com.example.hello_world.config.DynamoDbProperties;
import com.example.hello_world.entity.OrderEntity;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@ActiveProfiles("local")
@SpringBootTest
public class OrderRepositoryTest {
    
    @Autowired
    DynamoDbEnhancedClient client;
    @Autowired
    OrderRepository repository;
    @Autowired
    DynamoTestDataLoader loader;
    @Autowired
    DynamoDbProperties props;

    @BeforeEach
    void doBeforeEach(TestInfo testInfo) {
        
        DynamoDbTable<OrderEntity> table = loader.getTable(
            client,
            props.getTables().getOrders(),
            OrderEntity.class
        );
        String testName = testInfo.getTestMethod().get().getName();
        String filePath = "/repository/orders/" + testName + ".json";
        loader.loadData(filePath, table, OrderEntity.class);
    }

    @AfterEach
    void doAfterEach() {
        
        DynamoDbTable<OrderEntity> table = loader.getTable(
            client,
            props.getTables().getOrders(),
            OrderEntity.class
        );
        loader.deleteAllByPartitionKey(table, OrderEntity::getOrderId);
    }

    @Test
    @DisplayName("OrderIdをキーに1件取得できること")
    void test001() {
        String orderId1 = "order-001";
        OrderEntity entity1 = repository.getItemByOrderId(orderId1);

        assertNotNull(entity1);
        assertEquals(entity1.getOrderId(), "order-001");
        assertEquals(entity1.getUserId(), "user-001");
        assertEquals(entity1.getStatus(), "PAID");
        assertEquals(entity1.getTotalAmount(), 1500);
        assertEquals(entity1.getShippingPrefecture(), "Tokyo");
        assertEquals(entity1.getCreatedAt(), "2024-06-01T10:15:30+09:00");
        assertFalse(entity1.getDeleted());
        assertEquals(entity1.getTtl(), 1712136930L);

        String orderId2 = "order-002";
        OrderEntity entity2 = repository.getItemByOrderId(orderId2);

        assertNotNull(entity2);
        assertEquals(entity2.getOrderId(), "order-002");
        assertEquals(entity2.getUserId(), "user-002");
        assertEquals(entity2.getStatus(), "SHIPPED");
        assertEquals(entity2.getTotalAmount(), 2500);
        assertEquals(entity2.getShippingPrefecture(), "Osaka");
        assertEquals(entity2.getCreatedAt(), "2024-06-02T11:20:35+09:00");
        assertFalse(entity2.getDeleted());
        assertEquals(entity2.getTtl(), 1712223335L);
    }
}
