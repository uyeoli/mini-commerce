package com.cart.controller;

import com.cart.dto.CartCreateDto;
import com.cart.dto.CartModifyDto;
import com.cart.dto.CartResponseDto;
import com.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{memberId}")
    public ResponseEntity<CartResponseDto> getMyCart(@PathVariable Long memberId) {
        return ResponseEntity.ok(cartService.getMyCart(memberId));
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(@RequestBody CartCreateDto cartCreateDto) {
        cartService.addToCart(cartCreateDto);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(@PathVariable Long cartItemId,
                                               @RequestBody CartModifyDto cartModifyDto) {
        cartService.updateCartItem(cartItemId, cartModifyDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
