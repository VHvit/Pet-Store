package com.example.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private Integer userStatus;
}
