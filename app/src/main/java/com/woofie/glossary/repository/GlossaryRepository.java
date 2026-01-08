package com.woofie.glossary.repository;

import com.woofie.glossary.model.GlossaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface GlossaryRepository extends JpaRepository<GlossaryEntry, Integer> {
    List<GlossaryEntry> findByCreatedAtAfter(Instant timestamp);
}
