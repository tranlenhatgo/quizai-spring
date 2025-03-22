package com.myproject.quizzai.model;


import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Question {
    @DocumentId
    private String id;
    private String quiz_id;
    private String content;
    private String category_id;
    private String image;
    private String score;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
