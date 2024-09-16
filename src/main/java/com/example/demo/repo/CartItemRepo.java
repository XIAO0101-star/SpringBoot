package com.example.demo.repo;


import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
   
    List<CartItem> findByUser(User user);
   
    Optional<CartItem> findByUserAndProduct(User user, Product product);
   
    Optional<CartItem> findByUserAndId(User user, Long id);
   
    long countByUser(User user);
   
    void deleteByUser(User user);

    void deleteByIdAndUser(long id, User user);
}
