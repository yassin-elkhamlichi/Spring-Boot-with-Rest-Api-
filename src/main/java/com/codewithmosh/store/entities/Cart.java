package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "`Cart`")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id" , columnDefinition = "CHAR(36)")
    private UUID  id;
    @Column(name = "dateCreated")
    private Date dateCreated;
    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItem = new HashSet<>();

}
