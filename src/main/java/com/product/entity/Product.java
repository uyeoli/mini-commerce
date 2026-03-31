package com.product.entity;

import com.product.dto.request.ProductCreateDto;
import com.product.dto.request.ProductModifyDto;
import com.product.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "product", schema = "mini_commerce")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String productInformation;
    private Integer price;
    private int stock;
    private Category category;
    private String imageUrl;

    public static Product from(ProductCreateDto dto) {
        Product product = new Product();
        product.productName = dto.getProductName();
        product.productInformation = dto.getProductInformation();
        product.price = dto.getPrice();
        product.stock = dto.getStock();
        product.category = dto.getCategory();
        product.imageUrl = dto.getImageUrl();
        return product;
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void modify(ProductModifyDto productModifyDto) {
        this.productName = productModifyDto.getProductName();
        this.productInformation = productModifyDto.getProductInformation();
        this.price = productModifyDto.getPrice();
        this.stock = productModifyDto.getStock();
        this.category = productModifyDto.getCategory();
        if (productModifyDto.getImageUrl() != null) this.imageUrl = productModifyDto.getImageUrl();
    }
}
