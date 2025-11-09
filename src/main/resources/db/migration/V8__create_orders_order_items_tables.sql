CREATE TABLE orders
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT         NOT NULL,
    status      varchar(20)    NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME       NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    constraint fk_customer
        foreign key (customer_id)
            references users (id)
);

CREATE TABLE order_items
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT         NOT NULL UNIQUE ,
    product_id  BIGINT         NOT NULL UNIQUE,
    unit_price  DECIMAL(10, 2) NOT NULL,
    quantity    INT            NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    constraint fk_order
        foreign key (order_id)
            references orders (id),
    constraint  fk_product
        foreign key (product_id)
            references products (id)
)