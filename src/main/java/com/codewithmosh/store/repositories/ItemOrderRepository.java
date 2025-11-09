package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order_items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOrderRepository extends JpaRepository<Order_items,Long> {
        Boolean existsByProductId(Long id);
}
