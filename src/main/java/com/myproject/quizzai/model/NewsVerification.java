package com.myproject.quizzai.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsVerification {

    @DocumentId
    private String id;
    private String content;
    private Double accuracyPercentage;
    private Integer confirmedSources;
    private Integer totalSources;
    private List<SourceInfo> sources;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}