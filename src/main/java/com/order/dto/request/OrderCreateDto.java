package com.order.dto.request;

import lombok.Getter;

@Getter
public class OrderCreateDto {
    private Long memberId;
    private String deliveryAddress;
}
