package com.product.dto.response;

import com.product.entity.Product;
import com.product.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String productInformation;
    private Integer price;
    private int stock;
    private Category category;
    private String imageUrl;
    private boolean stockShortage;  // 재고 5개 이하 시 true

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productInformation(product.getProductInformation())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .stockShortage(product.getStock() <= 5)
                .build();
    }
}
