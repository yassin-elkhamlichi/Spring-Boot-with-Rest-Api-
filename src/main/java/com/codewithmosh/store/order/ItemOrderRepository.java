package com.codewithmosh.store.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemOrderRepository extends JpaRepository<Order_items,Long> {
        Boolean existsByProductId(Long id);

        Order_items findByProductIdAndOrderId(Long productId , Long orderId);

        boolean existsByOrderId(Long idOrder);
}
