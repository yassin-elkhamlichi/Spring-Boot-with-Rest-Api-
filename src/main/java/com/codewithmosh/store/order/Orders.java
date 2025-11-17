package com.codewithmosh.store.order;

import com.codewithmosh.store.products.Product;
import com.codewithmosh.store.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public void updateQuantityinItem(int quantity, Order_items item) {
        getOrder_items().forEach(
                item1 -> {
                    if (item.getProduct().getId().equals(item.getProduct().getId())) {
                        item.setQuantity(quantity);
                    }
                });
    }

    public void updateTotalAmount(int quantity ,Order_items item) {
        getOrder_items().forEach(
                item1 -> {
                    if (item.getProduct().getId().equals(item.getProduct().getId())) {
                        item.setTotal_amount(item.getUnit_price().multiply(BigDecimal.valueOf(quantity)));
                    }
                });
    }

    public Order_items getItemOrder(Long productId){
        return getOrder_items().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public Order_items addToOrder(Product product) {
        var order_items = getItemOrder(product.getId());
        int quantity = 1;
        if (order_items != null) {
            quantity = order_items.getQuantity() + 1;
        } else {
            order_items = Order_items.builder()
                    .product(product)
                    .order(this)
                    .build();
            order_items.setUnit_price(product.getPrice());
            getOrder_items().add(order_items);
            order_items.setQuantity(quantity);
        }
        updateQuantityinItem(quantity, order_items);
        updateTotalAmount(quantity, order_items);
        return order_items;
    }

    public void removeItemOrder(Long idProduct){
        Order_items item = getItemOrder(idProduct);
        getOrder_items().remove(item);
    }

    public void changeStatus(String status) {
        switch (status) {
            case "PAID":
                setStatus(Status.PAID);
                break;
            case "FAILED":
                setStatus(Status.FAILED);
                break;
            case "CANCELLED":
                setStatus(Status.CANCELLED);
                break;
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }
}
