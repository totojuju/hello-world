package com.example.hello_world.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.hello_world.DynamoTestDataLoader;
import com.example.hello_world.config.DynamoDbProperties;
import com.example.hello_world.entity.OrderEntity;
import com.example.hello_world.response.GetOrderHistoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@ActiveProfiles("local")
@AutoConfigureMockMvc
@SpringBootTest
public class GetOrderHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    DynamoDbEnhancedClient client;
    @Autowired
    DynamoTestDataLoader loader;
    @Autowired
    DynamoDbProperties props;

    private static final ObjectMapper mapper = new ObjectMapper();

    MultiValueMap<String, String> multiValueMap;

    @BeforeEach
    void doBeforeEach(TestInfo testInfo) throws Exception{
        
        DynamoDbTable<OrderEntity> table = loader.getTable(
            client,
            props.getTables().getOrders(),
            OrderEntity.class
        );
        String testName = testInfo.getTestMethod().get().getName();

        String filePath = "/controller/get_order_history/data/" + testName + ".json";
        loader.loadData(filePath, table, OrderEntity.class);

        String request = new String(new ClassPathResource("controller/get_order_history/request/" + testName + ".json").getInputStream().readAllBytes());
        Map<String, String> requestMap = mapper.readValue(request, Map.class);
        multiValueMap = new LinkedMultiValueMap<>();
        requestMap.forEach(multiValueMap::add);
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
    void test001() throws Exception {

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.get("/get-order-history")
                .queryParams(multiValueMap))
            .andExpect(status().isOk())
            .andReturn();

        GetOrderHistoryResponse response = mapper.readValue(
            result.getResponse().getContentAsString(), GetOrderHistoryResponse.class
        );
        assertNotNull(response);
        assertNotNull(response.getOrderHistoryList());
        assertEquals(1, response.getOrderHistoryList().size());
        assertEquals("order-001", response.getOrderHistoryList().get(0).getOrderId());
        assertEquals("user-001", response.getOrderHistoryList().get(0).getUserId());
        assertEquals("PAID", response.getOrderHistoryList().get(0).getStatus());
        assertEquals(1500L, response.getOrderHistoryList().get(0).getTotalAmount());
        assertEquals("Tokyo", response.getOrderHistoryList().get(0).getShippingPrefecture());
        assertEquals("2024-06-01T10:15:30+09:00", response.getOrderHistoryList().get(0).getCreatedAt());
    }
}
