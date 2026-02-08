package com.cart.service;

import com.cart.dto.CartCreateDto;
import com.cart.dto.CartModifyDto;
import com.cart.dto.CartResponseDto;
import com.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final StringRedisTemplate redisTemplate;
    private final CartRepository cartRepository;

    @Override
    public List<CartResponseDto> getMyCart(Long userId) {
        return cartRepository.getCartByUserId(userId);
    }

    @Override
    public void createCart(CartCreateDto cartCreateDto) {

    }

    @Override
    public void modifyCart(CartModifyDto cartModifyDto) {

    }
}
