package com.example.models.entity;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pets")
@Accessors (chain = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @Builder.Default
    @Type(type = "jsonb")
    private List<String> photoUrls = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tags_id", nullable = true)
    private TagEntity tags;

    @Column(nullable = true)
    private String status;
}