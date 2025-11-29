package com.yassineproject.store.payement;

import com.yassineproject.store.order.Order_items;
import com.yassineproject.store.order.Orders;
import com.yassineproject.store.order.Status;
import com.yassineproject.store.carts.CartEmptyException;
import com.yassineproject.store.carts.CartNotFoundException;
import com.yassineproject.store.carts.CartRepository;
import com.yassineproject.store.order.OrdersRepository;
import com.yassineproject.store.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final OrdersRepository ordersRepository;
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final IPaymentGateway paymentGateway;

    public void manageWebhookEvent(
           WebhookRequest request
    ){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(
                       paymentResult -> {
                           var order = ordersRepository.findById(paymentResult.getOrderId()).orElseThrow();
                           order.setStatus(paymentResult.getPaymentStatus());
                           ordersRepository.save(order);
                       }
                );

    }



    @Transactional
    public CheckOutResponseDto CheckingOut(CheckOutRequestDto data)  {
        var cart = cartRepository.findById(data.getCartId()).orElse(null);
        if(cart == null ){
            throw new CartNotFoundException();
        }
        if(cart.getItemCart().isEmpty()){
            throw new CartEmptyException();
        }
        Orders order = Orders.builder()
                .order_items(new ArrayList<>())
                .build();
        cart.getItemCart().forEach( itemCart ->
                {
                    Order_items itemOrder = Order_items.builder()
                            .order(order)
                            .product(itemCart.getProduct())
                            .quantity(itemCart.getQuantity())
                            .unit_price(itemCart.getProduct().getPrice())
                            .total_amount(cart.getTotalPrice())
                            .build();
                    order.getOrder_items().add(itemOrder);
                }
        );
        var user =authService.getCurrentUser();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotalAmount(cart.getTotalPrice().doubleValue());
        ordersRepository.save(order);

        try {
            //Create a checkout session
            var session = paymentGateway.createCheckoutSession(order);
            cartRepository.delete(cart);
            return new CheckOutResponseDto(order.getId() ,  session.getCheckoutUrl());
        } catch (PaymentException e) {
            ordersRepository.delete(order);
            throw e;
        }
    }

}
