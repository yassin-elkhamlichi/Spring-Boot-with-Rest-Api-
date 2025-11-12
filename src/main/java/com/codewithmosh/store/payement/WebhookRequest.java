package com.codewithmosh.store.payement;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebhookRequest {
    private String payload;
    private Map<String,String> headers;
}
