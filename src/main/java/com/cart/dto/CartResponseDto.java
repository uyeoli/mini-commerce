package com.cart.dto;

import com.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponseDto {
    private Long cartId;
    private Long memberId;
    private List<CartItem> cartItemList;
    private int totalPrice;

    public static CartResponseDto from(Cart cart) {
        List<CartItem> items = cart.getCartItems().stream()
                .map(CartItem::from)
                .toList();
        int total = items.stream().mapToInt(CartItem::getSubtotal).sum();
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .memberId(cart.getMember().getId())
                .cartItemList(items)
                .totalPrice(total)
                .build();
    }
}
