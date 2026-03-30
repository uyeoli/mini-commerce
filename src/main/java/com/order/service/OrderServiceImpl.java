package com.order.service;

import com.cart.entity.Cart;
import com.cart.repository.CartRepository;
import com.order.dto.request.OrderCreateDto;
import com.order.dto.request.OrderStatusUpdateDto;
import com.order.dto.response.OrderDetailResponseDto;
import com.order.dto.response.OrderResponseDto;
import com.order.entity.Order;
import com.order.entity.OrderItem;
import com.order.repository.OrderItemRepository;
import com.order.repository.OrderRepository;
import com.user.entity.Member;
import com.user.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    @Transactional
    public Long createOrder(OrderCreateDto dto) {
        Member member = memberJpaRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Cart cart = cartRepository.findByMemberId(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 비어있습니다."));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("장바구니에 상품이 없습니다.");
        }

        int totalPrice = cart.getCartItems().stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = orderRepository.save(Order.create(member, dto.getDeliveryAddress(), totalPrice));

        cart.getCartItems().forEach(cartItem -> {
            cartItem.getProduct().decreaseStock(cartItem.getQuantity());
            orderItemRepository.save(OrderItem.create(order, cartItem.getProduct(), cartItem.getQuantity()));
        });

        // 주문 완료 후 장바구니 비우기
        cart.getCartItems().clear();

        return order.getId();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatusUpdateDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.updateStatus(dto.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByMember(Long memberId) {
        return orderRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(OrderResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        return OrderDetailResponseDto.from(order);
    }
}
