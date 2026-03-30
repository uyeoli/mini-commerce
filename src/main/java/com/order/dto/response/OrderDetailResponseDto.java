package com.order.dto.response;

import com.order.entity.Order;
import com.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class OrderDetailResponseDto {
    private Long orderId;
    private String memberName;
    private OrderStatus status;
    private String statusDescription;
    private String deliveryAddress;
    private int totalPrice;
    private Instant createdAt;
    private List<OrderItemDto> items;

    public static OrderDetailResponseDto from(Order order) {
        List<OrderItemDto> items = order.getOrderItems().stream()
                .map(OrderItemDto::from)
                .toList();
        return OrderDetailResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .status(order.getStatus())
                .statusDescription(order.getStatus().getDescription())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
