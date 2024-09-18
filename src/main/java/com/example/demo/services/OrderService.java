package com.example.demo.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    public OrderService() {

    }

    @Transactional
    public void handleSuccessfulPayment(Event event) {
        System.out.println(event);
        System.out.println("STRIPE API VERSION = " + Stripe.API_VERSION);
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        String sessionId = session.getId();

        try {

            // expand each line item so that we can get the product id and quantity
            // Retrieve the session with line items expanded
             SessionListLineItemsParams listItemParams = SessionListLineItemsParams.builder()
                    .addExpand("data.price.product")
                    .build();
            List<LineItem> lineItems = session.listLineItems(listItemParams).getData();

            Map<String, Long> orderedProducts = new HashMap<>();

            for (LineItem item : lineItems) {
                String productId =  item.getPrice()
                                    .getProductObject()
                                    .getMetadata().
                                    get("product_id");
                if (productId == null || productId.isEmpty()) {
                    System.err.println("Product ID not found for: " + item.getId());
                    continue; // Skip this item
                }
                long quantity = item.getQuantity();
                orderedProducts.put(productId, quantity);
            }

            String customerId = session.getCustomer();
            String paymentIntentId = session.getPaymentIntent();
            System.out.println(orderedProducts);

            System.out.println("Payment was successful. PaymentIntent ID: " + paymentIntentId);
        } catch (StripeException e) {
            System.out.println(e);
        }

    }

    @Transactional
    public void handleInvoicePaid(Event event) {
        // Handle paid invoice, if applicable to your system
    }

    @Transactional
    public void handlePaymentFailed(Event event) {
        // Handle failed payment
        // You might want to notify the user, mark the order as failed, etc.
    }
}
