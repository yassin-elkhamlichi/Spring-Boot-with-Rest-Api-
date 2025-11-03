ALTER TABLE CartItem
ADD CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id);