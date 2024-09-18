package com.example.demo.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    
}