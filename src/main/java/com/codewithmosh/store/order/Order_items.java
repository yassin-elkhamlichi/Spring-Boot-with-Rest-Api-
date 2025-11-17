package com.codewithmosh.store.order;

import com.codewithmosh.store.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "order_items")
public class Order_items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "unit_price")
    private BigDecimal unit_price;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "total_price")
    private BigDecimal total_amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

}

