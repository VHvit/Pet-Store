package com.example.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(nullable = true)
    private String name;
}
