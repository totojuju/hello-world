// package com.example.hello_world.repository;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Repository;

// import com.example.hello_world.entity.UserEntity;

// import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
// import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
// import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
// import software.amazon.awssdk.enhanced.dynamodb.Expression;
// import software.amazon.awssdk.enhanced.dynamodb.Key;
// import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
// import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
// import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
// import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
// import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

// /**
//  * Usersテーブルアクセス用クラス.
//  */
// @Repository
// public class UsersRepository {
    
//     // Usersテーブル名
//     @Value("${dynamodb.tables.users}")
//     private String userTableName;

//     // DynamoDB拡張クライアント
//     @Autowired
//     DynamoDbEnhancedClient client;

//     // Usersテーブルオブジェクト
//     private final DynamoDbTable<UserEntity> table 
//             = client.table(userTableName, 
//                 TableSchema.fromBean(UserEntity.class));
    
//     private final DynamoDbIndex<UserEntity> gsi1Index 
//             = table.index("GSI1");
    
//     public List<UserEntity> queryByGsi1(String gsi1pValue, String gsi1sValue) {

//         // 検索条件の作成
//         QueryConditional queryConditional = QueryConditional
//                 .keyEqualTo(Key.builder().partitionValue(gsi1pValue).sortValue(gsi1sValue).build());
        
//         // クエリリクエスト作成
//         QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                 .queryConditional(queryConditional)
//                 .filterExpression(Expression.builder()
//                         .expression("deleted = :deleted")
//                         .putExpressionValue(":deleted", AttributeValue.builder().bool(false).build())
//                         .build())
//                 .build();
        
//         List<UserEntity> users = gsi1Index.query(request)
//                 .stream()
//                 .flatMap(page -> page.items().stream())
//                 .toList();
        
//         return users;
//     }

//     public void updateUser(String name) {

//         UserEntity user = new UserEntity();
//         user.setName(name);

//         UpdateItemEnhancedRequest<UserEntity> request = 
//                 UpdateItemEnhancedRequest.builder(UserEntity.class)
//                 .item(user)
//                 .ignoreNulls(true)
//                 .build();
        
//         table.updateItem(request);
//     }
// }
