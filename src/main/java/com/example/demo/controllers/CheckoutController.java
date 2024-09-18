package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.User;
import com.example.demo.services.CartItemService;
import com.example.demo.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final StripeService stripeService;
    private final CartItemService cartItemService;

    @Autowired
    public CheckoutController(StripeService stripeService, CartItemService cartItemService) {
        this.stripeService = stripeService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/create-checkout-session")
    public String createCheckoutSession(
        @AuthenticationPrincipal User user,
        Model model
    ) {
        try {
            String successUrl = "https://www.google.com";
            String cancelUrl = "https://www.yahoo.com";

            var cartItems = cartItemService.findByUser(user);
            Session session = stripeService.createCheckout(cartItems, user.getId(), successUrl, cancelUrl);
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("stripePublicKey", stripeService.getPublicKey());


            return "checkout/checkout";
        } catch (StripeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
