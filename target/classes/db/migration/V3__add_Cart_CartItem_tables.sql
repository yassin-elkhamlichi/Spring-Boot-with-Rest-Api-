CREATE TABLE Cart
(
    id           VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    date_created DATE NOT NULL
);

CREATE TABLE CartItem
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id    VARCHAR(36) NOT NULL,
    product_id BIGINT      NOT NULL,
    quantity   INT         NOT NULL,
    CONSTRAINT fk_cart_id FOREIGN KEY (cart_id) REFERENCES Cart (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE ON UPDATE CASCADE
);