package com.woofie.glossary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDto {
    private Integer id;
    private String sourceText;
    private String targetText;
    private List<TranslationConnectionDto> connections;
}