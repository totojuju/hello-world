package com.example.hello_world.query;

import java.util.List;

import com.example.hello_world.dto.OrderHistoryDto;

import lombok.Data;

@Data
public class GetOrderHistoryQueryResult {
    
    private List<OrderHistoryDto> orderHistoryList;
}
