package com.example.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;

public interface CartItemRepo extends JpaRepository<CartItem, Long>{
    // find all the cart items that belong to a user
    // findBy => "SELECT * FROM cart_items"
    // ByUser => "WHERE user_id = ?"
    List<CartItem> findByUser(User user);

    // findBy => "SELECT *"
    // ByUser => "WHERE user_id = ?"
    // and => "AND"
    // product => "product_id = ?"
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    // get a cart item by user and the ID of the cart item
    Optional<CartItem> findByUserAndId(User user, Long id);

    // countBy = "SELECT COUNT(*)"
    // ByUser = "FROM cart_items"
    long countByUser(User user);

    // "delete" => "DELETE FROM"
    // "byUser" => "WHERE user_account.id = ?""
    void deleteByUser(User user);

    void deleteByIdAndUser(long id, User user);
}