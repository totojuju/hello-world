// package com.example.hello_world.entity;

// import lombok.Data;
// import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
// import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

// @Data
// @DynamoDbBean
// public class UserEntity {
    
//     // ユーザーID
//     private String userId;
//     // ユーザー名
//     private String name;
//     // メールアドレス
//     private String email;
//     // 論理削除フラグ
//     private Boolean deleted;

//     @DynamoDbPartitionKey
//     public String getUserId() {
//         return userId;
//     }
// }
