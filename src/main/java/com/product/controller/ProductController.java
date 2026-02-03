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

    @GetMapping("/product")
    public ResponseEntity<List<ProductResponseDto>> getProduct(@RequestBody ProductSearchCondition condition) {
        return ResponseEntity.ok(productService.getProductByCondition(condition));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        productService.createProduct(productCreateDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modifyProduct(@PathVariable Long id, @RequestBody ProductModifyDto productModifyDto) {
        productService.modify(id, productModifyDto);
        return ResponseEntity.ok().build();
    }


}
