package com.codewithmosh.store.payement;

import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.entities.Status;
import com.codewithmosh.store.exception.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StripePaymentGateway implements IPaymentGateway{
    @Value("${spring.webSiteUrl}")
    private String webSiteUrl;
    @Value("${spring.stripe.webhookSecretKey}")
    private String webhookKey;

    @Override
    public CheckoutSession createCheckoutSession(Orders order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl + "/checkout-cancel?orderId=" + order.getId())
                    .putMetadata("order_id", order.getId().toString());
            order.getOrder_items().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
           var session =  Session.create(builder.build());
           return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var event = Webhook.constructEvent(request.getPayload(),request.getHeaders().get("stripe-signature"),webhookKey);
            var orderId = extractOrderId(event);
            return  switch (event.getType()){
                case "payment_intent.succeeded"->
                    Optional.of(new PaymentResult(orderId, Status.PAID));
                case "payment_intent.payment_failed" ->
                    Optional.of(new PaymentResult(orderId, Status.FAILED));
                default -> Optional.empty();

            };
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature");
        }
    }

    private Long extractOrderId(Event event){
        StripeObject stripObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                ()-> new PaymentException("Could not deserialize Stripe event , check the sdk and api version")
        );
        if (!(stripObject instanceof PaymentIntent paymentIntent)) {
            throw new PaymentException(
                    "Expected PaymentIntent but received: " + stripObject.getClass().getSimpleName()
            );
        }
        var orderId = paymentIntent.getMetadata().get("order_id");
        return Long.valueOf(orderId);

    }


    private SessionCreateParams.LineItem createLineItem(Order_items item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item)
                )
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(Order_items item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(
                        item.getUnit_price().multiply(BigDecimal.valueOf(100)))
                .setProductData(
                        createProductData(item)
                )
                .build();
    }

    private  SessionCreateParams.LineItem.PriceData.ProductData createProductData(Order_items item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}
