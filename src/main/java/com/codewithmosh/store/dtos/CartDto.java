package com.codewithmosh.store.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CartDto {
    private UUID id;
    @JsonIgnore
    private Date createdAt;
    private Set<ItemCartDto> items;
}
