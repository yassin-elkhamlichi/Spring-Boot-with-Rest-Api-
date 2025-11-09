package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepositroy extends JpaRepository<Orders, Long> {
}
