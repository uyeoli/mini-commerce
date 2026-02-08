package com.cart.service;

import com.cart.dto.CartModifyDto;
import com.cart.dto.CartResponseDto;
import com.cart.dto.CartCreateDto;

import java.util.List;

public interface CartService {

    List<CartResponseDto> getMyCart(Long userId);

    void createCart(CartCreateDto cartCreateDto);

    void modifyCart(CartModifyDto cartModifyDto);
}

