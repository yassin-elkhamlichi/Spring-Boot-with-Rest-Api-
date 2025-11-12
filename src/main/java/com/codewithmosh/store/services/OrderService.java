package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exception.*;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.mappers.Order_itemsMapper;
import com.codewithmosh.store.repositories.*;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final ItemOrderRepository itemOrderRepository;
    private final OrdersRepository ordersRepository;
    private final Order_itemsMapper order_itemsMapper;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final RestClient.Builder builder;

    @Value("${string.webSiteUrl}")
    private String webSiteUrl;


    public ItemCartDto addItemInOrder(AddItemToOrderDto data,Long idOrder){
        var product = productRepository.findById(data.getProductId()).orElse(null);
        if (product == null) {
           throw new ProductNotFoundException();
        }
        var order = ordersRepository.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        Order_items order_items = order.addToOrder(product) ;

        var orderItemDto = order_itemsMapper.toDto(order_items);
        orderItemDto.setTotalPrice(order_items.getTotal_amount());
        orderItemDto.getProduct().setProductId(order_items.getProduct().getId());
        ordersRepository.save(order);

        return orderItemDto;
    }

    public ItemCartDto updateItemInOrder(UpdateItemInOrder data, Long idOrder, Long idProduct) {
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        Orders order = ordersRepository.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        Order_items item = itemOrderRepository.findByProductIdAndOrderId(idProduct,idOrder);
        item.setQuantity(data.getQuantity());
        item.setTotal_amount(item.getUnit_price().multiply(BigDecimal.valueOf(data.getQuantity())));

        order.updateQuantityinItem(data.getQuantity(),item);
        order.updateTotalAmount(data.getQuantity(),item);

        ordersRepository.save(order);

        var orderItemdDto = order_itemsMapper.toDto(item);
        orderItemdDto.setTotalPrice(item.getTotal_amount());
        orderItemdDto.getProduct().setProductId(item.getProduct().getId());
        return orderItemdDto;
    }

    public void deleteItemFromOrder(Long idOrder, Long idProduct) {
        var product = productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        Orders order = ordersRepository.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        order.removeItemOrder(idProduct);
        ordersRepository.save(order);
    }

    public void deleteOrder(Long idOrder) {
        Orders order = ordersRepository.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        ordersRepository.delete(order);
    }

    public OrderDto changeStatus(Long idOrder, String status ) {
        Orders order = ordersRepository.findById(idOrder).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        User user = authService.getCurrentUser();
        if(user == null) {
            throw new UserNotFoundException();
        }
        order.changeStatus(status);
        ordersRepository.save(order);
        return orderMapper.toDto(order);
    }

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
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl + "\"/checkout-cancel?orderId="+order.getId());
            order.getOrder_items().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("DH")
                                        .setUnitAmountDecimal(item.getUnit_price())
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
                builder.addLineItem(lineItem);
            });
            var session = Session.create(builder.build());
            cartRepository.delete(cart);
            return new CheckOutResponseDto(order.getId() ,  session.getUrl());
        } catch (StripeException e) {
            ordersRepository.delete(order);
            throw new RuntimeException(e);
        }
    }
}
