package com.example.models;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Order {
    private Integer id;
    private Integer petId;
    private Integer quantity;
    private OffsetDateTime date;
    private String status;
    private boolean complete;
}




