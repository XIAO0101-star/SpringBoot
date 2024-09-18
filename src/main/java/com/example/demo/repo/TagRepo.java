package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag,Long> {
    
}
