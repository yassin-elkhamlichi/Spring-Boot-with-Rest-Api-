package com.codewithmosh.store.payement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckOutResponseDto {
    private Long OrderId;
    private String CheckOutUrl;
}
