package com.example.hello_world.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hello_world.criteria.OrderQueryByGsiOneCriteria;
import com.example.hello_world.dto.OrderHistoryDto;
import com.example.hello_world.entity.OrderEntity;
import com.example.hello_world.mapper.GetOrderHistoryMapper;
import com.example.hello_world.query.GetOrderHistoryQuery;
import com.example.hello_world.query.GetOrderHistoryQueryResult;
import com.example.hello_world.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetOrderHistoryService {
    
    private final GetOrderHistoryMapper getOrderHistoryMapper;
    private final OrderRepository orderRepository;

    public GetOrderHistoryQueryResult execute(GetOrderHistoryQuery query) {
        
        OrderQueryByGsiOneCriteria orderQueryByGsiOneCriteria = getOrderHistoryMapper.toOrderQueryByGsiOneCriteria(query);
        
        List<OrderEntity> orderList = orderRepository.queryByGsi1(orderQueryByGsiOneCriteria);

        List<OrderHistoryDto> orderHistoryList = orderList.stream()
                .map(orderEntity -> getOrderHistoryMapper.toOrderHistoryDto(orderEntity))
                .toList();
        
        GetOrderHistoryQueryResult result = new GetOrderHistoryQueryResult();
        result.setOrderHistoryList(orderHistoryList);
        return result;
    }
}
