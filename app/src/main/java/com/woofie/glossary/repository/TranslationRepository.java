package com.woofie.glossary.repository;

import com.woofie.glossary.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Integer> {
    List<Translation> findByUpdatedAtAfter(Long timestamp);
}