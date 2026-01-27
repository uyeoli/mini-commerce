package com.product.service;

import com.product.dto.request.ProductCreateDto;
import com.product.dto.request.ProductSearchCondition;
import com.product.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getProductByCondition(ProductSearchCondition condition);

    void createProduct(ProductCreateDto productCreateDto);

    void delete(Long id);

    void modify(Long id);
}
