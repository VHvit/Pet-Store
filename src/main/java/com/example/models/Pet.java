package com.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Pet {
    private long id;
    private Category category;
    private String name;
    private String photoUrls;
    private Tag tags;
    private String status;
}
