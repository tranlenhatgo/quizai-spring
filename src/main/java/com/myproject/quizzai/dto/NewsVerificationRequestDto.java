package com.myproject.quizzai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "News Verification Request DTO", accessMode = Schema.AccessMode.WRITE_ONLY)
public class NewsVerificationRequestDto {
    
    @NotBlank(message = "Query cannot be blank")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "News content or claim to be verified")
    private String query;
    
    @Schema(description = "Language code for the query (default: vi for Vietnamese)")
    private String language = "vi";
    
    @Schema(description = "Number of sources to analyze (default: 8)")
    private Integer maxSources = 8;
}