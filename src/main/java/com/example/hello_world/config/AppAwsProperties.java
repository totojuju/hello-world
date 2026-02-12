package com.example.hello_world.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.aws")
public class AppAwsProperties {
    
    private String region;
    private final Dynamodb dynamodb = new Dynamodb();

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public Dynamodb getDynamodb() {
        return dynamodb;
    }

    public static class Dynamodb {
        private String endpointUrl;

        public String getEndpointUrl() {
            return endpointUrl;
        }

        public void setEndpointUrl(String endpointUrl) {
            this.endpointUrl = endpointUrl;
        }
    }
}
