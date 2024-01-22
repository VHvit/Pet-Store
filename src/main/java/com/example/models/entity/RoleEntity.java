package com.example.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name="roles")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class RoleEntity {

    @Id
    @GeneratedValue
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = true)
    private String name;
}
