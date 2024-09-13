package com.example.demo.repo;

import com.example.demo.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
   

}