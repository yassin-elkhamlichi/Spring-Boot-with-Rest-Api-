package com.codewithmosh.store.carts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemCartRepository  extends JpaRepository<ItemCart, Long> {
    Boolean existsByProductIdAndCartId(Long productId, UUID cartId);
    ItemCart findByProductIdAndCartId(Long productId, UUID id);
}
