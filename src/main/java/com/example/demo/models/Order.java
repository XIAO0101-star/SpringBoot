package com.example.demo.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(nullable = false, unique = false)
  private User user;

  @Column(nullable = false)
  private String status;

  public Order() {
  }

  public Order(User user, String status) {
    this.user = user;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  // toString method
  @Override 
  public String toString() {
    return "Order{" +
      "id=" + id +
      ", user=" + user +
      ", status='" + status + '\'' +
      '}';
  }

  // equals and hashCode methods
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(status, order.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, status);
  }
}