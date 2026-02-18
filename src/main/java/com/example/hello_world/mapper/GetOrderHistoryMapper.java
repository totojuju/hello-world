package com.example.hello_world.mapper;

import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

import com.example.hello_world.criteria.OrderQueryByGsiOneCriteria;
import com.example.hello_world.dto.OrderHistoryDto;
import com.example.hello_world.entity.OrderEntity;
import com.example.hello_world.query.GetOrderHistoryQuery;
import com.example.hello_world.query.GetOrderHistoryQueryResult;
import com.example.hello_world.request.GetOrderHistoryRequest;
import com.example.hello_world.response.GetOrderHistoryResponse;

@Mapper(componentModel = "spring")
public interface GetOrderHistoryMapper {
    
    GetOrderHistoryQuery toQuery(GetOrderHistoryRequest request);

    OrderQueryByGsiOneCriteria toOrderQueryByGsiOneCriteria(GetOrderHistoryQuery query);

    OrderHistoryDto toOrderHistoryDto(OrderEntity orderEntity);

    GetOrderHistoryResponse toResponse(GetOrderHistoryQueryResult queryResult);

    default String map(OffsetDateTime value) {
        return value == null ? null : value.toString();
    }

    default OffsetDateTime map(String value) {
        return (value == null || value.isBlank()) ? null : OffsetDateTime.parse(value);
    }
}
