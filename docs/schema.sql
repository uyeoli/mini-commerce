-- =============================================
-- Mini Commerce DDL
-- Schema: mini_commerce
-- =============================================

CREATE SCHEMA IF NOT EXISTS mini_commerce DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE mini_commerce;

-- ---------------------------------------------
-- 1. member
-- ---------------------------------------------
CREATE TABLE member (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    login_id   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_member_login_id (login_id)
);

-- ---------------------------------------------
-- 2. product
-- ---------------------------------------------
CREATE TABLE product (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    product_name         VARCHAR(255) NOT NULL,
    product_information  TEXT,
    price                INT,
    stock                INT          NOT NULL DEFAULT 0,
    category             VARCHAR(50)  NOT NULL COMMENT 'CLOTHING|SHOES|BAGS|ACCESSORIES|SPORTSWEAR|OUTER',
    PRIMARY KEY (id)
);

-- ---------------------------------------------
-- 3. cart
-- ---------------------------------------------
CREATE TABLE cart (
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    member_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_member FOREIGN KEY (member_id) REFERENCES member (id)
);

-- ---------------------------------------------
-- 4. cart_item
-- ---------------------------------------------
CREATE TABLE cart_item (
    id         BIGINT NOT NULL AUTO_INCREMENT,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_item_cart    FOREIGN KEY (cart_id)    REFERENCES cart    (id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES product (id)
);

-- ---------------------------------------------
-- 5. orders
-- ---------------------------------------------
CREATE TABLE orders (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    member_id        BIGINT       NOT NULL,
    status           VARCHAR(50)  NOT NULL COMMENT 'ORDER_COMPLETE|PAYMENT_COMPLETE|SHIPPING|DELIVERY_COMPLETE',
    delivery_address VARCHAR(255) NOT NULL,
    total_price      INT          NOT NULL,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_member FOREIGN KEY (member_id) REFERENCES member (id)
);

-- ---------------------------------------------
-- 6. order_item
-- ---------------------------------------------
CREATE TABLE order_item (
    id         BIGINT NOT NULL AUTO_INCREMENT,
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    price      INT    NOT NULL COMMENT '주문 시점 단가',
    PRIMARY KEY (id),
    CONSTRAINT fk_order_item_order   FOREIGN KEY (order_id)   REFERENCES orders  (id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product (id)
);

-- ---------------------------------------------
-- DB 사용자 생성 (최초 1회)
-- ---------------------------------------------
-- CREATE USER 'MINI_COMMERCE_USER'@'localhost' IDENTIFIED BY 'MINICOMMERCEUSER';
-- GRANT ALL PRIVILEGES ON mini_commerce.* TO 'MINI_COMMERCE_USER'@'localhost';
-- FLUSH PRIVILEGES;
