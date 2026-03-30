package com.order.dto.request;

import com.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusUpdateDto {
    private OrderStatus status;
}
