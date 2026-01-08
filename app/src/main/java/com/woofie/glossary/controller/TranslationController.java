package com.woofie.glossary.controller;

import com.woofie.glossary.dto.TranslationDto;
import com.woofie.glossary.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService service;

    // GET /api/translations - запрос всех единиц перевода с timestamp
    @GetMapping
    public ResponseEntity<?> getAll() {
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", Instant.now().getEpochSecond());
        res.put("translations", service.getAll());
        return ResponseEntity.ok(res);
    }

    // POST /api/translations - добавить единицу перевода (201 Created)
    @PostMapping
    public ResponseEntity<TranslationDto> create(@RequestBody TranslationDto dto) {
        if (dto.getSourceText() == null || dto.getTargetText() == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    // POST /api/translations-fetch-updates - запрос обновлений по времени
    @PostMapping("-fetch-updates")
    public ResponseEntity<?> fetch(@RequestBody Map<String, Long> body) {
        if (!body.containsKey("timestamp")) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        // Здесь можно добавить фильтрацию по времени в сервисе
        return ResponseEntity.ok(service.getAll());
    }

    // GET /api/translations/{id} - запросить по индексу
    @GetMapping("/{id}")
    public ResponseEntity<TranslationDto> getOne(@PathVariable Integer id) {
        TranslationDto dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // PUT /api/translations/{id} - обновить или добавить
    @PutMapping("/{id}")
    public ResponseEntity<TranslationDto> update(@PathVariable Integer id, @RequestBody TranslationDto dto) {
        if (dto.getSourceText() == null || dto.getTargetText() == null) {
            return ResponseEntity.badRequest().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    // DELETE /api/translations/{id} - удалить по индексу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}