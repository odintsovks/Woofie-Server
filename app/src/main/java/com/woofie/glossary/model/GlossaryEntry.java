package com.woofie.glossary.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "glossary")
@Data
public class GlossaryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sourceTerm;

    @Column(nullable = false)
    private String targetTerm;

    @Column(columnDefinition = "TEXT")
    private String definition;

    @CreationTimestamp
    private Instant createdAt;
}