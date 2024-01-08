package com.example.models.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TagDto {
    private UUID id;
    private String name;
}