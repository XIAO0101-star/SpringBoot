package com.example.demo.repo;

import com.example.demo.models.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepo extends JpaRepository<Product,Long> {
    
    // Not SQL, it's JPQL
    // @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    // List<Product> findAllWithCategories();

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.tags")
    List<Product> findAllWithCategoriesAndTags();
}
