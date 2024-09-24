package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
  @Query("SELECT o FROM Order o")
  List<Order> findAll();
}
