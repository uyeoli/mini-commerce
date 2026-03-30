package com.cart.service;

import com.cart.dto.CartCreateDto;
import com.cart.dto.CartModifyDto;
import com.cart.dto.CartResponseDto;

public interface CartService {

    CartResponseDto getMyCart(Long memberId);

    void addToCart(CartCreateDto cartCreateDto);

    void updateCartItem(Long cartItemId, CartModifyDto cartModifyDto);

    void removeCartItem(Long cartItemId);
}
