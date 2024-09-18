package com.example.demo.services;

import com.example.demo.models.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    @Value("${stripe.api.publicKey}")
    private String stripePublicKey;



    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(List<CartItem> cartItems, String successUrl, String cancelUrl) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                    .builder()
                    .setName(item.getProduct().getName())
                    .putMetadata("product_id", item.getProduct().getId().toString())
                    .build();

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData
                    .builder()
                    .setCurrency("usd")
                    .setUnitAmount(item.getProduct().getPrice().multiply(new BigDecimal("100")).longValue())
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem
                    .builder()
                    .setPriceData(priceData)
                    .setQuantity((long) item.getQuantity())
                    .build();

            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams
                .builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(cancelUrl)
                .setSuccessUrl(successUrl)
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params);
    }

    public String getPublicKey() {
        return stripePublicKey;
    }
   
}
