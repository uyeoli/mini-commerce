package com.product.service;

import com.product.dto.request.ProductCreateDto;
import com.product.dto.request.ProductSearchCondition;
import com.product.dto.response.ProductResponseDto;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import com.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    @Override
    public List<ProductResponseDto> getProductByCondition(ProductSearchCondition condition) {
        return productRepositoryCustom.searchProducts(condition.getCategory(), condition.getName())
                .stream().map(ProductResponseDto::from).toList();
    }

    @Override
    public void createProduct(ProductCreateDto productCreateDto) {
        Product product = Product.from(productCreateDto);
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void modify(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("product doesn't exist"));
        productRepository.save(product);
    }
}
