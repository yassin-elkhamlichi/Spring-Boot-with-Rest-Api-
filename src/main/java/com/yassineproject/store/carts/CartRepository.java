package com.yassineproject.store.carts;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CartRepository  extends CrudRepository<Cart, UUID> {
    @EntityGraph(attributePaths = "itemCart.product")
    @Query("select c from Cart c where c.id = :cartId ")
    Cart getCartWithItemsAndProducts(@Param("cartId") UUID cartId);
}
