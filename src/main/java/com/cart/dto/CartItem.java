package com.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItem {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private Integer price;
    private int quantity;
    private int
            subtotal;

    public static CartItem from(com.cart.entity.CartItem entity) {
        return CartItem.builder()
                .cartItemId(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getProductName())
                .price(entity.getProduct().getPrice())
                .quantity(entity.getQuantity())
                .subtotal(entity.getProduct().getPrice() * entity.getQuantity())
                .build();
    }
}
