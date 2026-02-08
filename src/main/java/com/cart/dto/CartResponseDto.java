package com.cart.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartResponseDto {
    private Long cartId;
    private Long userId;
    private List<CartItem> cartItemList = new ArrayList<>();
}
