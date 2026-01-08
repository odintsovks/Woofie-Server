package com.woofie.glossary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlossaryEntryDto {
    private UUID id;
    private String targetTerm;
    private String sourceTerm;
    private String definition;
}