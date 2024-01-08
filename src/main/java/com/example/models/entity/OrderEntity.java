package com.example.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private PetEntity petId;

    @Column(nullable = true)
    private Integer quantity;

    @Column(nullable = true, columnDefinition = "date")
    private OffsetDateTime date;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private boolean complete;
}
