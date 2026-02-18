package com.example.hello_world.dto;

import lombok.Data;

@Data
public class OrderHistoryDto {
    
    // 注文ID
    private String orderId;
    // ユーザーID
    private String userId;
    // ステータス
    private String status;
    // 注文金額
    private Long totalAmount;
    // 配送先都道府県
    private String shippingPrefecture;
    // 作成日時
    private String createdAt;
}
