package com.product.dto.request;

import com.product.enums.Category;
import lombok.Getter;

@Getter
public class ProductSearchCondition {
    private Category category;
    private String name;
}
