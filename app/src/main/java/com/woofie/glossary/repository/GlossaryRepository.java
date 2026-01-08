package com.woofie.glossary.repository;

import com.woofie.glossary.model.GlossaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface GlossaryRepository extends JpaRepository<GlossaryEntry, UUID> {
    List<GlossaryEntry> findByCreatedAtAfter(Instant timestamp);
}