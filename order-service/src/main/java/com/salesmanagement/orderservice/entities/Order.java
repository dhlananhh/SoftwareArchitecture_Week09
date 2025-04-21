package com.salesmanagement.orderservice.entities;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    @ElementCollection
    private List<Long> productIds;
    private Double totalAmount;
    private String status; // e.g., PENDING, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
}