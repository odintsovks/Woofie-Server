package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TranslationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldManageTranslations() throws Exception {
        String translationJson = """
            {
                "sourceText": "Spring",
                "targetText": "Весна",
                "connections": [
                    { "description": "Season" },
                    { "description": "Framework" }
                ]
            }
            """;

        // 1. Тест создания (POST /api/translations)
        mockMvc.perform(post("/api/translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(translationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.connections.length()").value(2));

        // 2. Тест получения списка с оберткой (GET /api/translations)
        mockMvc.perform(get("/api/translations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translations").isArray())
                .andExpect(jsonPath("$.timestamp").exists());

        // 3. Тест ошибки 400 (POST без обязательного поля)
        mockMvc.perform(post("/api/translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sourceText\": \"Only\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn404ForMissingId() throws Exception {
        mockMvc.perform(get("/api/translations/9999"))
                .andExpect(status().isNotFound());
    }
}