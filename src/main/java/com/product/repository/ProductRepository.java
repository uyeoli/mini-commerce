package com.product.repository;

import com.product.entity.Product;
import com.product.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:name IS NULL OR p.productName LIKE %:name%)")
    List<Product> searchProducts(@Param("category") Category category,
                                 @Param("name") String name);


}


