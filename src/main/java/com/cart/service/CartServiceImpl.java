package com.cart.service;

import com.cart.dto.CartCreateDto;
import com.cart.dto.CartModifyDto;
import com.cart.dto.CartResponseDto;
import com.cart.entity.Cart;
import com.cart.entity.CartItem;
import com.cart.repository.CartItemRepository;
import com.cart.repository.CartRepository;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import com.user.entity.Member;
import com.user.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getMyCart(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));
        return CartResponseDto.from(cart);
    }

    @Override
    @Transactional
    public void addToCart(CartCreateDto dto) {
        Member member = memberJpaRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Cart cart = cartRepository.findByMemberId(dto.getMemberId())
                .orElseGet(() -> cartRepository.save(Cart.create(member)));

        // 동일 상품이 이미 있으면 수량 추가
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.updateQuantity(item.getQuantity() + dto.getQuantity()),
                        () -> cartItemRepository.save(CartItem.create(cart, product, dto.getQuantity()))
                );
    }

    @Override
    @Transactional
    public void updateCartItem(Long cartItemId, CartModifyDto dto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 항목입니다."));
        cartItem.updateQuantity(dto.getQuantity());
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new IllegalArgumentException("존재하지 않는 장바구니 항목입니다.");
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
