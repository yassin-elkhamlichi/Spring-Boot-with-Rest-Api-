package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID  id;
    @Column(name = "date_created")
    private Date date_created;
    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItem = new HashSet<>();

}
