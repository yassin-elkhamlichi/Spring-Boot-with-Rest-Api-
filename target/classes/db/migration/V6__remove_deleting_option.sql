ALTER TABLE CartItem
    DROP FOREIGN KEY fk_product_id;

ALTER TABLE CartItem
    ADD CONSTRAINT fk_product_id
    FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE;