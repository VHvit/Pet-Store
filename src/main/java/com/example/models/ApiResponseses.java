package com.example.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResponseses {
    private Integer code;
    private String type;
    private String message;
}

