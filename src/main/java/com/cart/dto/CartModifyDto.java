package com.cart.dto;

import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
@Transactional
public class CartModifyDto {
    private Long productId;
    private Integer quantity;
}
