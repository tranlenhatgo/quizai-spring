package com.myproject.quizzai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "Autocomplete Suggestion Response DTO", accessMode = Schema.AccessMode.READ_ONLY)
public class AutocompleteSuggestionDto {
    
    @Schema(description = "List of suggested queries based on input")
    private List<String> suggestions;
    
    @Schema(description = "Whether there are more suggestions available")
    private Boolean hasMore;
}