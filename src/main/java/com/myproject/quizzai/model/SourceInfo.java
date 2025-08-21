package com.myproject.quizzai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceInfo {
    private String title;
    private String url;
    private String source;
    private Boolean isReliable;
    private Boolean confirmsClaim;
    private String summary;
}