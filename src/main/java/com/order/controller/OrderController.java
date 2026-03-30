package com.order.controller;

import com.order.dto.request.OrderCreateDto;
import com.order.dto.request.OrderStatusUpdateDto;
import com.order.dto.response.OrderDetailResponseDto;
import com.order.dto.response.OrderResponseDto;
import com.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        Long orderId = orderService.createOrder(orderCreateDto);
        return ResponseEntity.status(201).body(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId,
                                                  @RequestBody OrderStatusUpdateDto dto) {
        orderService.updateOrderStatus(orderId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(orderService.getOrdersByMember(memberId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }
}
