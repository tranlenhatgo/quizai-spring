package com.myproject.quizzai.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @DocumentId
    private String id;
    private String question_id;
    private String content;
    private Boolean is_correct;
    private String image;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
