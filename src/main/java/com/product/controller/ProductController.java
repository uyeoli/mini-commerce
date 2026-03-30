package com.product.controller;

import com.product.dto.request.ProductCreateDto;
import com.product.dto.request.ProductModifyDto;
import com.product.dto.request.ProductSearchCondition;
import com.product.dto.response.ProductResponseDto;
import com.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(@ModelAttribute ProductSearchCondition condition) {
        return ResponseEntity.ok(productService.getProductByCondition(condition));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        productService.createProduct(productCreateDto);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modifyProduct(@PathVariable Long id, @RequestBody ProductModifyDto productModifyDto) {
        productService.modify(id, productModifyDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
