package com.cart.dto;

import lombok.Getter;

@Getter
public class CartAddDto {
    private Long memberId;
    private Long productId;
    private int quantity;
}
