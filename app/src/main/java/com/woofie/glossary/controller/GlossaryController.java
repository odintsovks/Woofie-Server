package com.woofie.glossary.controller;

import com.woofie.glossary.dto.GlossaryEntryDto;
import com.woofie.glossary.service.GlossaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GlossaryController {
    private final GlossaryService service;

    @GetMapping("/glossary")
    public ResponseEntity<?> getAll() {
        Map<String, Object> res = new HashMap<>();
        res.put("timestamp", Instant.now().getEpochSecond());
        res.put("entries", service.getAll());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/glossary-fetch-updates")
    public ResponseEntity<?> fetch(@RequestBody Map<String, Long> body) {
        if (!body.containsKey("timestamp")) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.getUpdates(body.get("timestamp")));
    }

    @PostMapping("/glossary")
    public ResponseEntity<GlossaryEntryDto> create(@RequestBody GlossaryEntryDto dto) {
        if (dto.getSourceTerm() == null || dto.getTargetTerm() == null || dto.getDefinition() == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @GetMapping("/glossary/{id}")
    public ResponseEntity<GlossaryEntryDto> getOne(@PathVariable Integer id) { // Integer
        GlossaryEntryDto dto = service.getById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/glossary/{id}")
    public ResponseEntity<GlossaryEntryDto> update(@PathVariable Integer id, @RequestBody GlossaryEntryDto dto) { // Integer
        if (dto.getSourceTerm() == null || dto.getTargetTerm() == null || dto.getDefinition() == null)
            return ResponseEntity.badRequest().build();
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/glossary/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { // Integer
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
