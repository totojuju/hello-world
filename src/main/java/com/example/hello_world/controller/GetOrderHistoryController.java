package com.example.hello_world.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hello_world.mapper.GetOrderHistoryMapper;
import com.example.hello_world.query.GetOrderHistoryQuery;
import com.example.hello_world.query.GetOrderHistoryQueryResult;
import com.example.hello_world.request.GetOrderHistoryRequest;
import com.example.hello_world.response.GetOrderHistoryResponse;
import com.example.hello_world.service.GetOrderHistoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequiredArgsConstructor
public class GetOrderHistoryController {

    private final GetOrderHistoryMapper mapper;
    private final GetOrderHistoryService service;
    
    @GetMapping("get-order-history")
    public ResponseEntity<GetOrderHistoryResponse> getMethodName(
            @ModelAttribute GetOrderHistoryRequest request) {
        
        GetOrderHistoryQuery query = mapper.toQuery(request);
        GetOrderHistoryQueryResult queryResult = service.execute(query);
        GetOrderHistoryResponse response = mapper.toResponse(queryResult);
        return ResponseEntity.ok(response);
    }
    
}
