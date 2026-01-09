package com.woofie.glossary.controller;

import com.woofie.glossary.dto.TranslatorApiDto;
import com.woofie.glossary.service.TranslatorApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TranslatorApiController {
    private final TranslatorApiService service;

    @GetMapping("/translation-services")
    public ResponseEntity<List<TranslatorApiDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/translate/{id}")
    public ResponseEntity<Map<String, String>> create(@PathVariable String id, @RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null) {
            return ResponseEntity.badRequest().build();
        }
        text = service.translate(id, text);
        if (text == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("text", text));
    }
}

