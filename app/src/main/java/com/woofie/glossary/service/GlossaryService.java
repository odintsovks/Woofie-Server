package com.woofie.glossary.service;

import com.woofie.glossary.dto.GlossaryEntryDto;
import com.woofie.glossary.model.GlossaryEntry;
import com.woofie.glossary.repository.GlossaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GlossaryService {
    private final GlossaryRepository repository;

    public List<GlossaryEntryDto> getAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<GlossaryEntryDto> getUpdates(long epoch) {
        return repository.findByCreatedAtAfter(Instant.ofEpochSecond(epoch))
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public GlossaryEntryDto save(GlossaryEntryDto dto) {
        GlossaryEntry entity = new GlossaryEntry();
        if (dto.getId() != null) entity.setId(dto.getId());
        entity.setSourceTerm(dto.getSourceTerm());
        entity.setTargetTerm(dto.getTargetTerm());
        entity.setDefinition(dto.getDefinition());
        return mapToDto(repository.save(entity));
    }

    public GlossaryEntryDto getById(UUID id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    private GlossaryEntryDto mapToDto(GlossaryEntry e) {
        return new GlossaryEntryDto(e.getId(), e.getTargetTerm(), e.getSourceTerm(), e.getDefinition());
    }
}