package com.woofie.glossary.service;

import com.woofie.glossary.dto.TranslationConnectionDto;
import com.woofie.glossary.dto.TranslationDto;
import com.woofie.glossary.model.Translation;
import com.woofie.glossary.model.TranslationConnection;
import com.woofie.glossary.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private final TranslationRepository repository;

    // 1. Для GET /translations
    public List<TranslationDto> getAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // 2. Для GET /translations/{id}
    public TranslationDto getById(Integer id) {
        return repository.findById(id).map(this::mapToDto).orElse(null);
    }

    // 3. Для POST /translations-fetch-updates
    public List<TranslationDto> getUpdates(Long timestamp) {
        // Предполагается, что в репозитории есть метод findByUpdatedAtAfter
        // Если поле в модели Long, используем простое сравнение
        return repository.findAll().stream()
                .filter(t -> t.getUpdatedAt() > timestamp)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // 4. Для POST и PUT /translations
    @Transactional
    public TranslationDto save(TranslationDto dto) {
        Translation entity;

        // Если ID пришел, пытаемся найти существующую запись (для PUT)
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(new Translation());
        } else {
            entity = new Translation();
        }

        entity.setSourceText(dto.getSourceText());
        entity.setTargetText(dto.getTargetText());

        // Обновляем метку времени при каждом сохранении
        entity.setUpdatedAt(Instant.now().getEpochSecond());

        // Очищаем старые связи и добавляем новые (orphanRemoval в Entity удалит их из БД)
        entity.getConnections().clear();
        if (dto.getConnections() != null) {
            for (TranslationConnectionDto cDto : dto.getConnections()) {
                TranslationConnection conn = new TranslationConnection();
                conn.setDescription(cDto.getDescription());
                conn.setTranslation(entity);
                entity.getConnections().add(conn);
            }
        }

        Translation saved = repository.save(entity);
        return mapToDto(saved);
    }

    // 5. Для DELETE /translations/{id}
    @Transactional
    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private TranslationDto mapToDto(Translation t) {
        List<TranslationConnectionDto> conns = t.getConnections().stream()
                .map(c -> new TranslationConnectionDto(c.getId(), c.getDescription()))
                .collect(Collectors.toList());
        return new TranslationDto(t.getId(), t.getSourceText(), t.getTargetText(), conns);
    }
}