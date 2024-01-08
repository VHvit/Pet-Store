package com.example.repository;

import com.example.models.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<PetEntity, UUID> {
    List<PetEntity> findByStatusIn(List<String> status);
}