package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.models.OrderItem;

import jakarta.transaction.Transactional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
  @Query("SELECT o FROM OrderItem o WHERE o.order.id = :orderId")
  List<OrderItem> findByOrder(@Param("orderId") Long orderId);

  @Modifying
  @Transactional
  @Query("DELETE FROM OrderItem o WHERE o.order.id = :orderId")
  void deleteByOrderId(@Param("orderId") Long orderId);
}
