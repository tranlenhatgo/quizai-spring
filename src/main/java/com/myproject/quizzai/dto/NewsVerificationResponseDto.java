package com.myproject.quizzai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "News Verification Response DTO", accessMode = Schema.AccessMode.READ_ONLY)
public class NewsVerificationResponseDto {
    
    @Schema(description = "Original query that was verified")
    private String query;
    
    @Schema(description = "Accuracy percentage (0-100)")
    private Integer accuracyPercentage;
    
    @Schema(description = "Number of sources that confirm the information")
    private Integer confirmedSources;
    
    @Schema(description = "Total number of sources analyzed")
    private Integer totalSources;
    
    @Schema(description = "Detailed explanation of the verification result")
    private String explanation;
    
    @Schema(description = "List of sources used for verification")
    private List<SourceInfo> sources;
    
    @Schema(description = "Overall verdict")
    private VerificationVerdict verdict;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceInfo {
        @Schema(description = "Title of the source")
        private String title;
        
        @Schema(description = "URL of the source")
        private String url;
        
        @Schema(description = "Publisher or domain name")
        private String publisher;
        
        @Schema(description = "Whether this source supports the claim")
        private Boolean supportsQuery;
        
        @Schema(description = "Credibility score of the source (0-100)")
        private Integer credibilityScore;
    }
    
    public enum VerificationVerdict {
        TRUE,
        MOSTLY_TRUE,
        MIXED,
        MOSTLY_FALSE,
        FALSE,
        INSUFFICIENT_DATA
    }
}