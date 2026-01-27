package com.product.enums;

public enum Category {
    CLOTHING("의류"),
    SHOES("신발"),
    BAGS("가방"),
    ACCESSORIES("액세서리"),
    SPORTSWEAR("스포츠웨어"),
    OUTER("아우터");

    private final String name;

    Category(String name) {
        this.name = name;
    }

}
