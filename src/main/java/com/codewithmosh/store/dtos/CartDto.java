package com.codewithmosh.store.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;


@Getter
@AllArgsConstructor
public class CartDto {
    private UUID id;
    @JsonIgnore
    private Date createdAt;
    private BigDecimal totalPrice;
    private Set<ItemCartDto> items = new HashSet<>();
}
