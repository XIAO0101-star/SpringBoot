package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.example.demo.models.User;
import com.example.demo.services.CartItemService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final StripeService stripeServices;
    private final CartItemService cartItemService;

    @Autowired
    public CheckoutController(StripeService stripeServices, CartItemService cartItemService) {
        this.stripeServices = stripeServices;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/create-checkout-session")
    public String createCheckoutSession(@AuthenticationPrincipal User user, Model model) {
       
        try {
            String successUrl = "https://localhost:8080/checkout/success";
            String cancelUrl = "https://localhost:8080/checkout/cancel";

            var cartItems = cartItemService.findByUser(user);
            Session session = stripeServices.createCheckoutSession(cartItems, successUrl, cancelUrl);

            model.addAttribute("sessionId", session.getId());
            model.addAttribute("stripePublicKey", stripeServices.getPublicKey());
            return "checkout/checkout";

        } catch (StripeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
