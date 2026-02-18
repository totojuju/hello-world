package com.example.hello_world.response;

import java.util.List;

import com.example.hello_world.dto.OrderHistoryDto;

import lombok.Data;

@Data
public class GetOrderHistoryResponse {
    
    private List<OrderHistoryDto> orderHistoryList;
}
