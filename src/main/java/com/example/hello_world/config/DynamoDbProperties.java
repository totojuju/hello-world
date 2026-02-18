package com.example.hello_world.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb")
public class DynamoDbProperties {
    
    private final Tables tables = new Tables();

    public Tables getTables() {
        return tables;
    }

    public static class Tables {
        private String orders;

        public String getOrders() {
            return orders;
        }

        public void setOrders(String orders) {
            this.orders = orders;
        }
    }
}
