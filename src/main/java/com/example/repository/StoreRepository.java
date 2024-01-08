package com.example.repository;

import com.example.models.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findById(UUID orderId);
}