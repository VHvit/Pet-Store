package com.example.models.dto;

import com.example.models.entity.PetEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderDto {
    private UUID id;
    private PetEntity petId;
    private Integer quantity;
    private OffsetDateTime date;
    private String status;
    private boolean complete;
}




