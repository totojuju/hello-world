package com.example.hello_world.request;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetOrderHistoryRequest {
    
    /** ユーザーID. */
    @NotBlank
    private String userId;
    /** 開始日時. */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime fromDateTime;
    /** 終了日時. */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime toDateTime;
}
