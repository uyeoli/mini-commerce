package com.product.entity;

import com.product.dto.request.ProductCreateDto;
import com.product.enums.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "product", schema = "mini_commerce")
public class Product {
    @Id
    private Long id;
    private String productName;
    private String productInformation;
    private Integer price;
    private int stock;
    private Category category;

    public static Product from(ProductCreateDto dto) {
        Product product = new Product();
        product.productName = dto.getProductName();
        product.productInformation = dto.getProductInformation();
        product.price = dto.getPrice();
        product.stock = dto.getStock();
        return product;
    }

}
