package com.woofie.glossary.service;

import com.woofie.glossary.dto.TranslatorApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.lang.Exception;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class TranslatorApiService {
    private interface TranslatorApi {
        public String getName();
        public boolean checkAvailability();
        public String translate(String text);
    }

    private class LibreTranslateApi implements TranslatorApi {
        public String getName() { return "LibreTranslate (self-hosted)"; }
        public boolean checkAvailability() {
            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/health"))
                    .GET()
                    .build();

                try {
                    HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
                    return response.statusCode() == 200;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        public String translate(String text) {
            try (HttpClient client = HttpClient.newHttpClient()) {
              HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create("http://127.0.0.1:5000/translate"))
                  .POST(HttpRequest.BodyPublishers.ofString("{\"q\": \"" + text.replaceAll("\\", "\\\\").replaceAll("\"", "\\\"") + "\", \"source\": \"en\", \"taregt\": \"ru\"}"))
                  .build();

              try {
                  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                  return response.body();
              } catch (Exception e) {
                  return null;
              }
            }
        }
    }

    private Map<String, TranslatorApi> apis = Map.of(
          "libretranslate", new LibreTranslateApi()
    );
    
    public List<TranslatorApiDto> getAll() {
        return apis.entrySet().stream()
            .filter((Map.Entry<String, TranslatorApi> entry)->entry.getValue().checkAvailability())
            .map((Map.Entry<String, TranslatorApi> entry)->new TranslatorApiDto(entry.getKey(), entry.getValue().getName()))
            .toList();
    }

    public String translate(String id, String text) {
        TranslatorApi translator = apis.get(id);
        if (translator == null)
          return null;
        return translator.translate(text);
    }
}

