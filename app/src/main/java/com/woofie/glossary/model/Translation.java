package com.woofie.glossary.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "translations")
@Data
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String targetText;

    @Column(nullable = false, columnDefinition = "INT")
    private Integer xPosition;

    @Column(nullable = false, columnDefinition = "INT")
    private Integer yPosition;

    private Long updatedAt = Instant.now().toEpochMilli();

    // Связь: один перевод может иметь много доп. описаний (connections)
    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranslationConnection> connections = new ArrayList<>();
}
