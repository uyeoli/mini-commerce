package com.config.enums;

import com.product.enums.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String value) {
        return Category.valueOf(value.toUpperCase());
    }
}
