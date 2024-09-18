package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import com.example.demo.models.User;
import com.example.demo.models.Product;
import com.example.demo.models.CartItem;
import com.example.demo.services.CartItemService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CartController(CartItemService cartItemService, ProductService productService, UserService userService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userService = userService;
    }

    // - when a route includes the Principal in the parameters,
    // the Principal will information about the currently logged in user
    // - redirect attributes are for the flash message
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Principal principal,
            RedirectAttributes redirectAttributes

    ) {

        try {
            // get the current logged in user
            User user = userService.findUserByUsername(principal.getName());

            // find the product
            // .orElseThrow() -> performs a .get(); however if .isPresent() returns false
            // throws an exception
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            cartItemService.addToCart(user, product, quantity);

            redirectAttributes.addFlashAttribute("message",
                    String.format("Added %d %s to your cart", quantity, product.getName()));

            return "redirect:/cart";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("Error when adding product: %s", e.getMessage()));
            return "redirect:/products";
        }

    }

    @GetMapping("")
    public String viewCart(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<CartItem> cartItems = cartItemService.findByUser(user);
        model.addAttribute("cartItems", cartItems);

        return "cart/index";
    }

    @PostMapping("/{cartItemId}/updateQuantity")
    public String updateQuantity(@PathVariable long cartItemId,
            @RequestParam int newQuantity,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userService.findUserByUsername(principal.getName());
            cartItemService.updateQuantity(cartItemId, user, newQuantity);
            redirectAttributes.addFlashAttribute("message", "Quantity updated");
            return "redirect:/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }


     
    }

    @GetMapping("/{cartItemId}/remove")
    public String removeFromCart(@PathVariable long cartItemId,
        Principal principal,
        RedirectAttributes redirectAttributes) {
            
            try {
                var user = userService.findUserByUsername(principal.getName());
                cartItemService.removeFromCart(cartItemId, user);
                redirectAttributes.addFlashAttribute("message", "The item has been deleted");
             
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
               
            }
            return "redirect:/cart";
            
        }

}