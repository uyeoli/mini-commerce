package com.product.dto.request;

import com.product.enums.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {
    private Category category;
    private String name;
}
