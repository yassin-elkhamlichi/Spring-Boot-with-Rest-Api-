package com.codewithmosh.store.entities;

import com.codewithmosh.store.repositories.ItemCartRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id" , columnDefinition = "CHAR(36)")
    private UUID  id;
    @Column(name = "dateCreated")
    private Date dateCreated;
    @OneToMany(mappedBy = "cart" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<ItemCart> itemCart = new HashSet<>();
    public BigDecimal getTotalPrice() {
        return itemCart.stream().map(ItemCart::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ItemCart getItemCart(Long Product) {
        return  itemCart.stream()
                .filter(item -> item.getProduct().getId().equals(Product)).
                findFirst().
                orElse(null);
    }

    public ItemCart addItemCart(Product product ) {
        var itemCart = getItemCart(product.getId());
        int totalQuantity = 1;
        if (itemCart == null)
        {
            itemCart = new ItemCart();
            itemCart.setCart(this);
            itemCart.setProduct(product);
            itemCart.setQuantity(totalQuantity);
            getItemCart().add(itemCart);

        }else {
            itemCart.setQuantity(itemCart.getQuantity() + 1);
        }
        return itemCart;
    }

    public void removeItemCart(Long productId) {
        var itemCArt = getItemCart(productId);
        if(itemCart != null) {
            itemCart.remove(itemCArt);
            itemCArt.setCart(null);
        }
    }
    public void clear(){
        itemCart.clear();
    }
}
