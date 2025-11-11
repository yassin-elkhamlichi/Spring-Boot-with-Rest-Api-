package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @EntityGraph(attributePaths = {"item.products"})
    @Query("SELECT o FROM Orders o WHERE o.user = :user")
    List<Orders> getAllByUser(@Param("user") User user);
}
