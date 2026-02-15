package com.example.hello_world.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {
    
    private String regionName;
    private String endpointUrl;
    private String accessKey;
    private String secretKey;

    public DynamoDbConfig(AppAwsProperties appAwsProperties) {
        this.regionName = appAwsProperties.getRegion();
        this.endpointUrl = appAwsProperties.getDynamodb().getEndpointUrl();
        this.accessKey = appAwsProperties.getAccessKey();
        this.secretKey = appAwsProperties.getSecretKey();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        var builder = DynamoDbClient.builder()
                .region(Region.of(regionName));

        if (endpointUrl != null && !endpointUrl.isEmpty()) {
            builder.endpointOverride(java.net.URI.create(endpointUrl));
        }

        if (accessKey != null && !accessKey.isEmpty()
                && secretKey != null && !secretKey.isEmpty()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            ));
        }

        return builder.build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
