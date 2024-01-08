package com.example.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@Entity
@Table(name = "pets")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryEntity category;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<String> photoUrls = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tags_id", nullable = true)
    private TagEntity tags;

    @Column(nullable = true)
    private String status;
}