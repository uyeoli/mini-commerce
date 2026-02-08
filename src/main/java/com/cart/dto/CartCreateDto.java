package com.cart.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartCreateDto {
    private Long userId;
    private List<CartItem> cartItemList = new ArrayList<>();
}
