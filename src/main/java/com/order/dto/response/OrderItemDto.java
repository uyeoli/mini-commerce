package com.order.dto.response;

import com.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private int price;
    private int subtotal;

    public static OrderItemDto from(OrderItem item) {
        return OrderItemDto.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getProductName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
