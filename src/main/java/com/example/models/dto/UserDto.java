package com.example.models.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder

public class UserDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Integer userStatus;
}
