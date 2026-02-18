package com.example.hello_world.request;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetOrderHistoryRequest {
    
    /** ユーザーID. */
    @NotBlank
    private String userId;
    /** 開始日時. */
    private OffsetDateTime fromDateTime;
    /** 終了日時. */
    private OffsetDateTime toDateTime;
}
