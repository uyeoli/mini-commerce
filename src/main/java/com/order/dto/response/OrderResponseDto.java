package com.order.dto.response;

import com.order.entity.Order;
import com.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String memberName;
    private OrderStatus status;
    private String statusDescription;
    private int totalPrice;
    private Instant createdAt;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .status(order.getStatus())
                .statusDescription(order.getStatus().getDescription())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
