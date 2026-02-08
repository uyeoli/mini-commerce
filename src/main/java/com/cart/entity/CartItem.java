package com.cart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CartItem {

    @Id
    private Long id;
}
