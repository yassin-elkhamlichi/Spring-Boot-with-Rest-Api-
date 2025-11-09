package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "created_at" , insertable = false , updatable = false)
    private LocalDateTime orderDate;
    @Column(name = "total_price")
    private Double totalAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Order_items> order_items;
}
