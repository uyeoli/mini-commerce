package com.cart.dto;

import lombok.Getter;

@Getter
public class CartItem {
    private Long productId;
    private Long cartId;
    private Integer quantity;
}
