package com.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class User {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Integer userStatus;
}
