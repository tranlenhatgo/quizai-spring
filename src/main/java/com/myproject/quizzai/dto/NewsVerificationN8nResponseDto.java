package com.myproject.quizzai.dto;

import com.myproject.quizzai.model.SourceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsVerificationN8nResponseDto {
    private Output output;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {
        private Double accuracy;
        private Integer confirmedSources;
        private Integer totalSources;
        private List<SourceInfo> sources;
        private String analysis;
        private String status;
    }
}