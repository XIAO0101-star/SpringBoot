package com.example.demo.controllers;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.services.CartItemService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;


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

    @GetMapping("")
        public String viewCart(Model model, Principal principal) {
            User user = userService.findUserByUsername(principal.getName());
            List<CartItem> cartItems = cartItemService.findByUser(user);
            model.addAttribute("cartItems", cartItems);
       
            return "cart/index";
        }

    @GetMapping("/{cartItemId}/remove")
    public String removeFromCart(@PathVariable Long cartItemId,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
    try {
        User user = userService.findUserByUsername(principal.getName());
        cartItemService.removeFromCart(cartItemId, user);
        redirectAttributes.addFlashAttribute("message", "Item removed from cart");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
        return "redirect:/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                        @RequestParam(defaultValue = "1") int quantity,
                        Principal principal,
                        RedirectAttributes redirectAttributes) {
    try {
        // get the user
        User user = userService.findUserByUsername(principal.getName());
        Product product = productService.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        cartItemService.addToCart(user, product, quantity);
           
        redirectAttributes.addFlashAttribute("message",
                String.format("Added %d %s to your cart", quantity,
                product.getName())
        );

        return "redirect:/cart";

    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/products";
    }

    }

    @PostMapping("/{cartItemId}/updateQuantity")
    public String updateCartItemQuantity(@PathVariable long cartItemId, @RequestParam int newQuantity, Principal principal, RedirectAttributes redirectAttributes  ) {
        try {
            User user = userService.findUserByUsername(principal.getName());
            cartItemService.updateQuantity(cartItemId, user, newQuantity);
            redirectAttributes.addFlashAttribute("message", "Quantity updated");
            return "redirect:/cart";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }
}
