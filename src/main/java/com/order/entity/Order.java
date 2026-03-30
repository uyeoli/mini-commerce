package com.order.entity;

import com.order.enums.OrderStatus;
import com.user.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders", schema = "mini_commerce")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private int totalPrice;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order create(Member member, String deliveryAddress, int totalPrice) {
        Order order = new Order();
        order.member = member;
        order.status = OrderStatus.ORDER_COMPLETE;
        order.deliveryAddress = deliveryAddress;
        order.totalPrice = totalPrice;
        order.createdAt = Instant.now();
        return order;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }
}
