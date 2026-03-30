package com.product.service;

import com.product.dto.request.ProductCreateDto;
import com.product.dto.request.ProductModifyDto;
import com.product.dto.request.ProductSearchCondition;
import com.product.dto.response.ProductResponseDto;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import com.product.repository.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductByCondition(ProductSearchCondition condition) {
        return productRepositoryCustom.searchProducts(condition.getCategory(), condition.getName())
                .stream().map(ProductResponseDto::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return ProductResponseDto.from(product);
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateDto productCreateDto) {
        Product product = Product.from(productCreateDto);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void modify(Long id, ProductModifyDto productModifyDto) {
        Product product = productRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("product doesn't exist"));
        product.modify(productModifyDto);
    }
}
