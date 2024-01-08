package com.example.models.dto;

import com.example.models.entity.PetEntity;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderDto {
    private UUID id;
    private PetEntity petId;
    private Integer quantity;
    private OffsetDateTime date;
    private String status;
    private boolean complete;
}




