package com.marketplace.cartservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 10)
  private Double total;

  @Column(name = "STATUS", nullable = false, length = 50)
  private String status;

  @Column(name = "ORDER_DATE", nullable = false, columnDefinition = "TIMESTAMP(6)")
  private LocalDateTime orderDate;

}