package com.cart.controller;

import com.cart.dto.CartResponseDto;
import com.cart.dto.CartCreateDto;
import com.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDto>> getMyCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getMyCart(userId));
    }

    @PostMapping
    public ResponseEntity<Void> createCart(@RequestBody CartCreateDto cartCreateDto) {
        cartService.createCart(cartCreateDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> modifyCart(@RequestBody CartCreateDto cartCreateDto) {
        return ResponseEntity.ok().build();
    }

}
