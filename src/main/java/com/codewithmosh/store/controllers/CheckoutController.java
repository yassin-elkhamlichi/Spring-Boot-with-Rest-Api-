package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckOutRequestDto;
import com.codewithmosh.store.dtos.CheckOutResponseDto;
import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.exception.CartEmptyException;
import com.codewithmosh.store.exception.CartNotFoundException;
import com.codewithmosh.store.exception.PaymentException;
import com.codewithmosh.store.services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    @Value("${spring.stripe.webhookSecretKey}")
    private String webhookKey;

    @PostMapping
    public CheckOutResponseDto checkOut(
            @Valid @RequestBody CheckOutRequestDto request
            )
    {
        return orderService.CheckingOut(request);

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void>  handleWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ){
        try {
            var event = Webhook.constructEvent(payload,signature,webhookKey);
            System.out.println(event.getType());
            var stripObject = event.getDataObjectDeserializer().getObject().orElse(null);
            switch (event.getType()){
                case "payment_intent.succeeded"->{

                }
                case "payment_intent.failed" ->{

                }
            }
            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class , CartEmptyException.class})
    public ResponseEntity<Map<String,String>> handleException(Exception ex){
        return ResponseEntity.status(400).body(
                Map.of("error" , ex.getMessage())
        );
    }

}
