INSERT INTO categories (id, name) VALUES
                                      (1, 'Electronics'),
                                      (2, 'Fashion'),
                                      (3, 'Home & Kitchen'),
                                      (4, 'Sports & Outdoors'),
                                      (5, 'Books');

INSERT INTO products (name, price, description, category_id) VALUES
-- Electronics
('Apple iPhone 14', 799.00, '6.1-inch OLED display, A15 Bionic chip, 12MP dual-camera system.', 1),
('Sony WH-1000XM5 Headphones', 349.99, 'Premium noise-cancelling wireless headphones with 30-hour battery life.', 1),

-- Fashion
('Nike Air Force 1', 110.00, 'Classic basketball shoes with durable leather upper and soft cushioning.', 2),
('Adidas Hoodie Originals', 69.99, 'Comfortable cotton hoodie with the classic Adidas Originals logo.', 2),

-- Home & Kitchen
('Instant Pot Duo 7-in-1', 89.00, 'Electric pressure cooker with 7 cooking modes: slow cook, steam, sauté, and more.', 3),
('Philips Air Fryer XXL', 199.99, 'High-capacity air fryer using Rapid Air technology for crispy results.', 3),

-- Sports & Outdoors
('Wilson Evolution Basketball', 69.95, 'Indoor game basketball with microfiber composite leather for elite performance.', 4),
('Nike Resistance Bands Set', 25.50, 'Set of 3 resistance bands for strength workouts and mobility training.', 4),

-- Books
('Atomic Habits – James Clear', 16.99, 'Best-selling self-improvement book about building good habits.', 5),
('Clean Code – Robert C. Martin', 32.00, 'A handbook of agile software craftsmanship for writing maintainable code.', 5);
