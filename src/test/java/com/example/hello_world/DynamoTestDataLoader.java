package com.example.hello_world;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

@Component
public class DynamoTestDataLoader {
    
    /**
     * 指定のJSONファイルからDynamoDBテーブルにデータをロード.
     */
    public <T> void loadData(String filePath, DynamoDbTable<T> table, Class<T> itemClass) {

        List<T> items = jsonToItems(filePath, itemClass);
        for (T item : items) {
            table.putItem(item);
        }
    }

    /**
     * JSONファイルからDynamoDBアイテムリストを読み込み.
     */
    private <T> List<T> jsonToItems(String filePath, Class<T> itemClass) {
        
        try (InputStream is = getClass().getResourceAsStream(filePath)) {

            ObjectMapper mapper = new ObjectMapper();

            JavaType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, itemClass);
            
            return mapper.readValue(is, listType);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load test data from file: " + filePath, e);
        }
    }

    /**
     * 指定のパーティションキーで全アイテム削除.
     */
    public <T> void deleteAllByPartitionKey(DynamoDbTable<T> table, Function<T, String> partitionKeyExtractor) {

        PageIterable<T> pages = table.scan();
        pages.items().forEach(item -> table.deleteItem(
            Key.builder()
                .partitionValue(partitionKeyExtractor.apply(item))
                .build()
        ));
    }

    /**
     * テーブルオブジェクト取得.
     */
    public <T> DynamoDbTable<T> getTable(
        DynamoDbEnhancedClient client, String tableName, Class<T> itemClass) {
        return client.table(tableName, TableSchema.fromBean(itemClass));
    }
}
