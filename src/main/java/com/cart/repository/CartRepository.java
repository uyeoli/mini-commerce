package com.cart.repository;

import com.cart.dto.CartResponseDto;
import com.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartResponseDto> getCartByUserId(Long userId);
}
