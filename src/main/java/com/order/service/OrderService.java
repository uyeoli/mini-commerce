package com.order.service;

import com.order.dto.request.OrderCreateDto;
import com.order.dto.request.OrderStatusUpdateDto;
import com.order.dto.response.OrderDetailResponseDto;
import com.order.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderCreateDto orderCreateDto);

    void updateOrderStatus(Long orderId, OrderStatusUpdateDto dto);

    List<OrderResponseDto> getOrdersByMember(Long memberId);

    OrderDetailResponseDto getOrderDetail(Long orderId);
}
