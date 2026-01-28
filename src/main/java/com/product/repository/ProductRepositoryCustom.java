package com.product.repository;

import com.product.entity.Product;
import com.product.enums.Category;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import static com.product.entity.QProduct.product;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Product> searchProducts(Category category, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (category != null) {
            builder.and(product.category.eq(category));
        }

        if (name != null) {
            builder.and(product.productName.contains(name));
        }

        return queryFactory
                .selectFrom(product)
                .where(builder)
                .fetch();
    }
}
