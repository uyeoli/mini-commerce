package com.product.dto.request;

import com.product.enums.Category;
import lombok.Getter;

@Getter
public class ProductCreateDto {
    private String productName;
    private String productInformation;
    private Integer price;
    private int stock;
    private Category category;
}
