package com.example.hello_world.criteria;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class OrderQueryByGsiOneCriteria {
    
    /** ユーザーID. */
    private String userId;
    /** 開始日時. */
    private OffsetDateTime fromDateTime;
    /** 終了日時. */
    private OffsetDateTime toDateTime;
}
