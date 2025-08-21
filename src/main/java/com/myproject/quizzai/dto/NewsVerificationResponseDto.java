package com.myproject.quizzai.dto;

import com.myproject.quizzai.model.SourceInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "News Verification Response", accessMode = Schema.AccessMode.READ_ONLY)
public class NewsVerificationResponseDto {
    
    @Schema(description = "Unique verification ID")
    private String id;
    
    @Schema(description = "Original content that was verified")
    private String content;
    
    @Schema(description = "Accuracy percentage (0-100)")
    private Double accuracyPercentage;
    
    @Schema(description = "Number of sources that confirm the claim")
    private Integer confirmedSources;
    
    @Schema(description = "Total number of sources analyzed")
    private Integer totalSources;
    
    @Schema(description = "List of sources analyzed")
    private List<SourceInfo> sources;
    
    @Schema(description = "Status of the verification")
    private String status;
    
    @Schema(description = "Summary message for the verification result")
    private String summary;
}