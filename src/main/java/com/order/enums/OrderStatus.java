package com.order.enums;

public enum OrderStatus {
    ORDER_COMPLETE("주문완료"),
    PAYMENT_COMPLETE("결제완료"),
    SHIPPING("배송중"),
    DELIVERY_COMPLETE("배송완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
