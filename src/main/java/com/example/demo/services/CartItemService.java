package com.example.demo.services;
import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.CartItemRepo;
import com.example.demo.controllers.CartController;


import jakarta.transaction.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class CartItemService {
    // reference to cart item repository
    private final CartItemRepo cartItemRepo;  

    @Autowired
    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Transactional
    public CartItem addToCart(User user, Product product, int quantity) {
        // does the item already exist in the cart?
        Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(user, product);
       
        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            return cartItemRepo.save(cartItem);
        } else {
            CartItem newItem = new CartItem(user, product, quantity);
            return cartItemRepo.save(newItem);
        }
    }

    public List<CartItem> findByUser(User user) {
        return cartItemRepo.findByUser(user);
    }
    
    @Transactional
    public void updateQuantity(long cartItemId, User user, int newQuantity) {
        CartItem existingItem = cartItemRepo.findByUserAndId(user, cartItemId).orElseThrow( () -> new IllegalArgumentException("No cart item with this id"));
        existingItem.setQuantity(newQuantity);
        cartItemRepo.save(existingItem);
    }



    @Transactional
    public void removeFromCart(long cartItemId, User user) {
       cartItemRepo.deleteByIdAndUser(cartItemId, user);
    }
}
