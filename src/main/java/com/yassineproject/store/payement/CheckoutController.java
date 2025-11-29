package com.yassineproject.store.payement;

import com.yassineproject.store.common.ErrorDto;
import com.yassineproject.store.carts.CartEmptyException;
import com.yassineproject.store.carts.CartNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;


    @PostMapping
    public CheckOutResponseDto checkOut(
            @Valid @RequestBody CheckOutRequestDto request
            )
    {
        return checkoutService.CheckingOut(request);

    }

    @PostMapping("/webhook")
    public void  handleWebhook(
            @RequestHeader Map<String,String> headers,
            @RequestBody String payload
    ){
        checkoutService.manageWebhookEvent(new WebhookRequest(payload,headers));
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
