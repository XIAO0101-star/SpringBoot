package com.example.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String name;

   @Column(columnDefinition = "TEXT")
   private String description;

   @Column(nullable = false, precision = 10, scale = 2)
   private BigDecimal price;

   // Default constructor
   public Product() {}

   // Constructor with all fields except id
   public Product(String name, String description, BigDecimal price) {
       this.name = name;
       this.description = description;
       this.price = price;
   }

   // Getters and setters
   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   public String getDescription() {
       return description;
   }

   public void setDescription(String description) {
       this.description = description;
   }

   public BigDecimal getPrice() {
       return price;
   }

   public void setPrice(BigDecimal price) {
       this.price = price;
   }

   // toString method
   @Override
   public String toString() {
       return "Product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               '}';
   }

   // equals and hashCode methods
   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       Product product = (Product) o;
       return Objects.equals(id, product.id) &&
               Objects.equals(name, product.name) &&
               Objects.equals(description, product.description) &&
               Objects.equals(price, product.price);
   }

   @Override
   public int hashCode() {
       return Objects.hash(id, name, description, price);
   }
}