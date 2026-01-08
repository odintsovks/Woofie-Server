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
public class GlossaryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateAndReturnGlossary() throws Exception {
        String json = """
            {
                "sourceTerm": "Test",
                "targetTerm": "Тест",
                "definition": "Описание"
            }
            """;

        // Проверка POST (201 Created)
        mockMvc.perform(post("/glossary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Проверка GET (Формат из ТЗ)
        mockMvc.perform(get("/glossary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.entries").isArray());
    }
}